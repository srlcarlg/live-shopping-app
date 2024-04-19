package live.chat.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import live.chat.LiveResponse;
import live.chat.grpc.LiveGrpcService;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.channel.AbortedException;

public class WebSocketServer implements WebSocketHandler {

	@Autowired 
	private LiveGrpcService liveGrpcService;
	
	public static Map<String, Sinks.Many<String>> publishers = new ConcurrentHashMap<>();
    public static Map<String, MessageSubscriber> subscribers = new ConcurrentHashMap<>();

	@Override
	public Mono<Void> handle(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath();
        String slugRoom = path.substring(path.lastIndexOf('/') + 1);
                
        publishers.computeIfAbsent(slugRoom, p -> Sinks.many().multicast().onBackpressureBuffer());
        subscribers.computeIfAbsent(slugRoom, s -> new MessageSubscriber(publishers.get(slugRoom)));
        
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
				}
			}).subscribe();
        };
        
        // Workaround (maybe)
        // To get the session of any incoming message in the subscriber
        MessageInfo messageInfo = new MessageInfo(session);
        
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
    	// on every first connection if the WS Server doesn't have ANY active session.
    	// Is the reason of .onErrorComplete
        session.send(Mono.just(session.textMessage(msg)))
        .then(session.close())
        .onErrorComplete().subscribe();
    }
    
	private static class MessageSubscriber {
	    private Sinks.Many<String> msgPublisher; 

	    public MessageSubscriber(Sinks.Many<String> msgPublisher) {
	    	this.msgPublisher = msgPublisher;
	    }

	    public void onNext(MessageInfo messageInfo) {
	    	WebSocketSession session = messageInfo.getSession();
	    		        	    	
	    	System.out.println(messageInfo.getSession());
	        System.out.println(msgPublisher.currentSubscriberCount());
	        sendToAll(messageInfo.message);
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
		
		public MessageInfo(WebSocketSession session) {
			this.session = session;
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
	}
}
