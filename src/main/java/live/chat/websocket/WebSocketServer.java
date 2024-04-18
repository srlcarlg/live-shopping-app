package live.chat.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class WebSocketServer implements WebSocketHandler {

    Map<String, Sinks.Many<String>> publishers = new ConcurrentHashMap<>();
    Map<String, MessageSubscriber> subscribers = new ConcurrentHashMap<>();

	@Override
	public Mono<Void> handle(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath();
        String slugRoom = path.substring(path.lastIndexOf('/') + 1);
        
        publishers.computeIfAbsent(slugRoom, p -> Sinks.many().multicast().onBackpressureBuffer());
        subscribers.computeIfAbsent(slugRoom, s -> new MessageSubscriber(publishers.get(slugRoom)));

        MessageSubscriber subscriber = subscribers.get(slugRoom);
        
        // Workaround (maybe)
        // To get the session of any incoming message in the subscriber
        MessageInfo message = new MessageInfo(session);
        
        session.receive()
		        .map(WebSocketMessage::getPayloadAsText)
		        .map(message::toMessageInfo)
    	        .subscribe(
		            subscriber::onNext, 
		            subscriber::onError, 
		            subscriber::onComplete
		        );
		
        // Keep Connection
        return session.send(publishers.get(slugRoom).asFlux().map(session::textMessage));
	}


	private static class MessageSubscriber {
	    private Sinks.Many<String> msgPublisher; 

	    public MessageSubscriber(Sinks.Many<String> msgPublisher) {
	    	this.msgPublisher = msgPublisher;
	    }

	    public void onNext(MessageInfo messageInfo) {
	    	System.out.println(messageInfo.getSession());
	        System.out.println(msgPublisher.tryEmitNext(messageInfo.getMessage()));
	        System.out.println(msgPublisher.currentSubscriberCount());
	    }

	    public void onError(Throwable error) {
	        error.printStackTrace(); 
	    }

	    public void onComplete() {
	        msgPublisher.tryEmitComplete();
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
