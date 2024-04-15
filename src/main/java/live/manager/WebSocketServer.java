package live.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.vertx.VertxContextSupport;
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
import live.manager.decoders.sub.SetBroadcaster;
import live.manager.decoders.sub.SetLiveStatus;
import live.manager.encoders.SendMessage;
import live.manager.encoders.SendMessageEncoder;
import live.manager.encoders.sub.BroadcasterId;
import live.manager.encoders.sub.UsersCount;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;

@ServerEndpoint(
	value = "/live/{slug}",
	decoders = { ReceivedMessageDecoder.class },
	encoders = {SendMessageEncoder.class }
)
@ApplicationScoped
public class WebSocketServer {

	protected static final Map<String, ConcurrentLinkedDeque<Session>> ROOMS = new ConcurrentHashMap<>();
	protected static final Map<String, String> BROADCASTERS_ID = new ConcurrentHashMap<>();
	
	protected static final ConcurrentLinkedDeque<Thread> VIRTUAL_THREADS = new ConcurrentLinkedDeque<>();
	// TODO: Scheduled closing of virtual threads

	@OnOpen
	public void onOpen(Session session, @PathParam("slug") String slug) {

		Panache.withSession(() -> Live.findBySlug(slug)).subscribe().with(item -> {
			if (item == null) {
				sendAsync(session, new SendMessage("slug-not-found"));
				closeSession(session);
			} else {
				if (item.getStatus().equals(LiveStatus.DONE)) {
					sendAsync(session,  new SendMessage("live-finished"));
					closeSession(session);
				} else {
					// Add to Room
					ROOMS.computeIfAbsent(slug, s -> new ConcurrentLinkedDeque<>());
					if (!ROOMS.get(slug).contains(session)) {
						ROOMS.get(slug).add(session);
					}
					BROADCASTERS_ID.computeIfAbsent(slug, s -> "");
				}
			}
		}, failure -> {
			sendAsync(session, new SendMessage("FAIL"));
		});
	}

	@OnClose
	public void onClose(Session session, @PathParam("slug") String slug) {
		if (ROOMS.containsKey(slug)) {
			ROOMS.get(slug).remove(session);
			sendToAll(slug, new UsersCount(ROOMS.get(slug).size()), session);
		}
	}

	@OnError
	public void onError(Session session, @PathParam("slug") String slug, Throwable throwable) {
		System.out.println(String.format("onError %s -> %s", slug, throwable.getLocalizedMessage()));
	}

	@OnMessage
	public void onMessage(Session session, ReceivedMessage message, @PathParam("slug") String slug) {

		if (message instanceof SetBroadcaster) {
			// Workaround to "IllegalStateException: No current Vertx context found",
			// even using Panache.withSession() and .withTransaction() like in the @OnOpen method.
			// and also we cannot block the current Vert.x Thread with VertxContextSupport.subscribeAndAwait()
			Thread vtThread = Thread.startVirtualThread(() -> {
				try { setBroadcaster(session, slug, message); } catch (Throwable e) {}
			});
			VIRTUAL_THREADS.add(vtThread);

		} else if (message instanceof SetLiveStatus) {
			Thread vtThread = Thread.startVirtualThread(() -> {
				try { setLiveStatus(session, slug, message); } catch (Throwable e) {}
			});
			VIRTUAL_THREADS.add(vtThread);
			
		} else {
			switch (message.getString()) {
			case "join":
				if (!BROADCASTERS_ID.get(slug).equals("")) {
					sendToAll(slug, new BroadcasterId(BROADCASTERS_ID.get(slug)), session);
				}
				sendToAll(slug, new UsersCount(ROOMS.get(slug).size()), session);
				break;

			case "leave":
				ROOMS.get(slug).remove(session);
				sendToAll(slug, new UsersCount(ROOMS.get(slug).size()), session);
				closeSession(session);
				break;

			default:
				sendAsync(session, new SendMessage("unknown-command"));
				break;
			}
		}
	}

	public void setBroadcaster(Session session, String slug, ReceivedMessage message) throws Throwable {
		VertxContextSupport.subscribeAndAwait(() -> {
			Panache.withSession(() -> Live.findBySlug(slug)).subscribe().with(item -> {
				if (item.password.equals(message.getLivePassword())) {
					BROADCASTERS_ID.put(slug, message.getJsPeerId());
					sendToAllInclude(slug, new BroadcasterId(message.getJsPeerId()));
				} else {
					sendAsync(session, new SendMessage("incorrect-password"));
				}
			}, failure -> {});
			// Better than NullPointerException of Panache.withSession(null)
			// or doing panache.withSession(() -> Live.findBySlug(slug)) again
			return Panache.currentTransaction();
		});
	}

	public void setLiveStatus(Session session, String slug, ReceivedMessage message) throws Throwable {
		VertxContextSupport.subscribeAndAwait(() -> {
			Panache.withSession(() -> Live.findBySlug(slug)).subscribe().with(item -> {
				if (item.password.equals(message.getLivePassword())) {
					
					Thread vtThread = Thread.startVirtualThread(() -> {
						try { changeLiveStatus(slug); } catch (Throwable e) {}
					});
					VIRTUAL_THREADS.add(vtThread);
					sendToAll(slug, new SendMessage("live-finished"), session);
					
				} else {
					sendAsync(session, new SendMessage("incorrect-password"));
				}
			}, failure -> {});
			// Better than NullPointerException of Panache.withSession(null)
			// or doing panache.withSession(() -> Live.findBySlug(slug)) again
			return Panache.currentTransaction();
		});
	}

	private void changeLiveStatus(String slug) throws Throwable   {
		VertxContextSupport.subscribeAndAwait(() -> {
			return Panache.withTransaction(() -> Live.findBySlug(slug).onItem().ifNotNull()
					.invoke(live -> live.setStatus(LiveStatus.DONE)));
		});
	}

	private void sendToAll(String slug, SendMessage message, Session currentSession) {
		ROOMS.get(slug).parallelStream().forEach(session -> {
			if (!session.equals(currentSession)) {
				sendAsync(session, message);
			}
		});
	}

	private void sendToAllInclude(String slug, SendMessage message) {
		ROOMS.get(slug).parallelStream().forEach(session -> {
			sendAsync(session, message);
		});
	}

	private void sendAsync(Session s, SendMessage message) {
		s.getAsyncRemote().sendObject(message, result -> {
			if (result.getException() != null) {
				System.out.println(String.format("Unable to send message: %s", result.getException()));
			}
		});
	}

	private void closeSession(Session session) {
		try { session.close(); } catch (Exception e) {}
	}
}
