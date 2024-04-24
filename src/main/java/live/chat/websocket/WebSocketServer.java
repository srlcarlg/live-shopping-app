package live.chat.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import live.chat.grpc.ChatGrpcServer;
import live.chat.grpc.LiveGrpcService;
import live.chat.grpc.live.LiveResponse;
import live.chat.websocket.database.ChatMessage;
import live.chat.websocket.database.ChatMessageRepository;
import live.chat.websocket.decoders.ReceivedMessage;
import live.chat.websocket.decoders.ReceivedMessageDecoder;
import live.chat.websocket.decoders.sub.JoinChat;
import live.chat.websocket.decoders.sub.MessageChat;
import live.chat.websocket.encoders.SendChat;
import live.chat.websocket.encoders.SendChatEncoder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class WebSocketServer implements WebSocketHandler {
	
	@Autowired LiveGrpcService liveGrpcService;
	@Autowired ChatMessageRepository chatMessageRepository;
	
	private static Map<String, Sinks.Many<String>> publishers = new ConcurrentHashMap<>();
	private static Map<String, MessageSubscriber> subscribers = new ConcurrentHashMap<>();

	@Override
	public Mono<Void> handle(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath();
        String slugRoom = path.substring(path.lastIndexOf('/') + 1);
                
        publishers.computeIfAbsent(slugRoom, p -> Sinks.many().multicast().onBackpressureBuffer());
        subscribers.computeIfAbsent(slugRoom, s -> new MessageSubscriber(
        	publishers.get(slugRoom), chatMessageRepository, liveGrpcService
        ));
        
        // Validate Slug and Status
        Runnable validate = () -> {
	        liveGrpcService.findBySlug(slugRoom)
			.doOnNext(live -> {
				if (live.equals(LiveResponse.getDefaultInstance())) {
					sendMsgAndClose(session, "slug-not-found");
					publishers.remove(slugRoom);
					subscribers.remove(slugRoom);
				} else if (live.getStatus().equals("DONE")) {
					sendMsgAndClose(session, "live-finished");
					publishers.remove(slugRoom);
					subscribers.remove(slugRoom);
				} else {
					ChatGrpcServer.slugs.add(slugRoom);
				}
			}).subscribe();
        };
        
        // Workaround (maybe)
        // To get the session of any incoming message in the subscriber
        MessageInfo messageInfo = new MessageInfo(session, slugRoom);
        
        MessageSubscriber subscriber = subscribers.get(slugRoom);
        session.receive()
        		.doFirst(validate)
		        .map(WebSocketMessage::getPayloadAsText)
		        .map(messageInfo::toMessageInfo)
    	        .subscribe(
		            subscriber::onNext, 
		            subscriber::onError, 
		            subscriber::onComplete
		        );
		
        // Keep Connection
        return session.send(publishers.get(slugRoom).asFlux().map(session::textMessage));
	}
	
    public static void sendMsgAndClose(WebSocketSession session, String msg) {
    	// Will throw AbortedException: Connection has been closed BEFORE send operation
    	// on every first connection if the WS Server doesn't have ANY session connected.
    	// Is the reason of .onErrorComplete
        session.send(Mono.just(session.textMessage(msg)))
        .then(session.close())
        .onErrorComplete().subscribe();
    }
    
	private static class MessageSubscriber {
		
	    private static LiveGrpcService liveService;
	    private static ChatMessageRepository messageRepository;
	    private Sinks.Many<String> msgPublisher; 
	    
	    private Map<WebSocketSession, SessionCredencials> credencials = new ConcurrentHashMap<>();
	    private boolean lockBroadcaster = false;
	    
	    public MessageSubscriber(Sinks.Many<String> msgPublisher,
	    		ChatMessageRepository messageRepository, LiveGrpcService liveService) {
	    	
	    	this.msgPublisher = msgPublisher;
	    	MessageSubscriber.liveService = liveService;
	    	MessageSubscriber.messageRepository = messageRepository;
	    }

	    public void onNext(MessageInfo messageInfo) {
	    	WebSocketSession session = messageInfo.getSession();
	    	String slug = messageInfo.getSlug();
	    	
	    	ReceivedMessage msg = ReceivedMessageDecoder.decode(messageInfo.getMessage());
	    	
	    	if (msg instanceof JoinChat) {
	    		// Add session
	    		credencials.computeIfAbsent(session, c -> 
	    			new SessionCredencials(msg.getUsername(), msg.getEmail(), false)
	    		);
	    		
	    		// If password exists, set current session as Broadcaster
	    		// and lock to avoid 2 broadcasters in the same room
	    		if (msg.getPassword() != null && !lockBroadcaster) {
		    		liveService.validate(slug, msg.getPassword())
		    		.doOnNext(v -> {
		    			if (v.getIsValid()) {
		    				credencials.get(session).setIsBroadcaster(true);
		    				lockBroadcaster = true;
		    				// sends sessionID then add to gRPC server
		    				sendToOne(session, session.getId());
		    				ChatGrpcServer.broadcastersIDs.put(slug, session.getId());
		    			} else {
		    				sendToOne(session, "incorrect-password");
		    			}
		    		}).subscribe();
	    		}
	    		
	    		// Send chat messages history
	    		messageRepository.getLastMessagesByLiveSlug(slug)
	    		.map(ChatMessage::toSendChat)
	    		.doOnNext(sc -> {
	    			sendToOne(session, SendChatEncoder.encoder(sc));
	    		})
	    		.subscribe();
	    		
	    	} else if (msg instanceof MessageChat) {
	    		// If session in joined
	    		if (credencials.containsKey(session)) {
	    			// Get some previous info and sends message to all sessions
	    			SendChat sendChat = credencials.get(session).toSendChat();
	    			sendChat.setMessage(msg.getMessage());
	    			sendToAll(SendChatEncoder.encoder(sendChat));
	    			// then save.
	    			messageRepository.save(new ChatMessage(slug, sendChat)).subscribe();
	    			
	    		} else {
	    			sendToOne(session, "unauthorized");
	    		}
	    		
	    	} else {
	    		// Only if session is Broadcaster
	    		if (msg.getString().contentEquals("finish-chat")) {
	    			if (!credencials.containsKey(session) || (!credencials.get(session).getIsBroadcaster())) {
		    			sendToOne(session, "unauthorized");
	    			} else {
		    			sendToAll("chat-finished");
	    			}
	    		} else { 
	    			sendToOne(session, "unknown-command");
	    		}
	    	}
	    }

	    public void onError(Throwable error) {
	        error.printStackTrace(); 
	    }

	    public void onComplete() {
	        msgPublisher.tryEmitComplete();
	    }
	    
	    private void sendToOne(WebSocketSession session, String msg) {
            session.send(Mono.just(session.textMessage(msg))).subscribe();
	    }
	    
	    private void sendToAll(String msg) {
	    	msgPublisher.tryEmitNext(msg);
	    }
	}
	
	private static class MessageInfo {
		private WebSocketSession session;
		private String message;
		private String slug;
		
		public MessageInfo(WebSocketSession session, String slug) {
			this.session = session;
			this.slug = slug;
		}
		
		public MessageInfo toMessageInfo(String message) {
			this.message = message;
			return this;
		}
		
		public WebSocketSession getSession() {
			return session;
		}
		public String getMessage() {
			return message;
		}
		public String getSlug() {
			return slug;
		}
	}
	
	private static class SessionCredencials {
		private String username;
		private String email;
		private Boolean isBroadcaster;
		
		public SessionCredencials(String username, String email, Boolean isBroadcaster) {
			super();
			this.username = username;
			this.email = email;
			this.isBroadcaster = isBroadcaster;
		}
		
		public SendChat toSendChat() {
			return new SendChat("", username, email, isBroadcaster);
		}
		
		public Boolean getIsBroadcaster() {
			return isBroadcaster;
		}
		
		public void setIsBroadcaster(Boolean isBroadcaster) {
			this.isBroadcaster = isBroadcaster;
		}
	}
}
