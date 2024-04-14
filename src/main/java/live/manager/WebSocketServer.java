package live.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import live.manager.decoders.ReceivedMessage;
import live.manager.decoders.ReceivedMessageDecoder;
import live.manager.decoders.sub.SetLiveStatus;
import live.manager.decoders.sub.SetBroadcaster;
import live.manager.encoders.SendMessage;
import live.manager.encoders.SendMessageEncoder;
import live.manager.encoders.sub.BroadcasterId;
import live.manager.encoders.sub.UsersCount;

@ServerEndpoint(
	value = "/live/{slug}",
	decoders = { ReceivedMessageDecoder.class },
	encoders = { SendMessageEncoder.class }
)
@ApplicationScoped
public class WebSocketServer {

	protected static final Map<String, ConcurrentLinkedDeque<Session>> ROOMS = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("slug") String slug) {
		ROOMS.computeIfAbsent(slug, s -> new ConcurrentLinkedDeque<>());
		if (!ROOMS.get(slug).contains(session)) {
			ROOMS.get(slug).add(session);	
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("slug") String slug) {
		ROOMS.get(slug).remove(session);
	}

	@OnError
	public void onError(Session session, @PathParam("slug") String slug, Throwable throwable) {
		ROOMS.get(slug).remove(session);
	}

	@OnMessage
	public void onMessage(Session session, ReceivedMessage message, @PathParam("slug") String slug) {

		if (message instanceof SetBroadcaster) {
			sendToAll(slug, new BroadcasterId("some-great-Id"));
			
		} else if (message instanceof SetLiveStatus) {
			sendToAll(slug, new SendMessage("live-finished"));
			
		} else {
			switch (message.getString()) {
			case "join":
				sendToAll(slug, new UsersCount(ROOMS.get(slug).size()));
				break;
			case "leave":	
				sendToAll(slug, new SendMessage("leaving..."));
				break;
			default: 
			    sendAsync(session, new SendMessage("unknown-command"));
			    break;
			}
		}		
	}

	private void sendToAll(String slug, SendMessage message) {
		ROOMS.get(slug).parallelStream().forEach(session -> sendAsync(session, message));
	}

	private void sendAsync(Session s, SendMessage message) {
		s.getAsyncRemote().sendObject(message, result -> {
			if (result.getException() != null) {
				// System.out.println(String.format(
				// "Unable to send message: %s", result.getException()));
			}
		});
	}
}
