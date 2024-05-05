package live.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Uni;
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
	public static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

	@OnOpen
	public void onOpen(Session session, @PathParam("slug") String slug) {

		Panache.withSession(() -> Live.findBySlug(slug)).subscribe().with(item -> {
			if (item == null) {
				// Validate slug
				sendAsync(session, new SendMessage("slug-not-found"));
				closeSession(session);
			} else {
				// Close session if liveStatus.Done
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
		}, failure -> sendAsync(session, new SendMessage("FAIL")));
	}

	@OnClose
	public void onClose(Session session, @PathParam("slug") String slug) {
		// Remove from Room then sends users-count to all
		if (ROOMS.containsKey(slug)) {
			ROOMS.get(slug).remove(session);
			sendToAllInclude(slug, new UsersCount(ROOMS.get(slug).size()));
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
			executorService.submit(() -> {
				try { setBroadcaster(session, slug, message); } catch (Throwable e) {}
			});
			
		} else if (message instanceof SetLiveStatus) {
			executorService.submit(() -> {
				try { setLiveStatus(session, slug, message); } catch (Throwable e) {}
			});
			
		} else {
			switch (message.getString()) {
			case "join":
				// Send broadcaster's id to current session
				if (!BROADCASTERS_ID.get(slug).equals("")) {
					sendAsync(session, new BroadcasterId(BROADCASTERS_ID.get(slug)));
				}
				// Send users-count to all 
				sendToAllInclude(slug, new UsersCount(ROOMS.get(slug).size()));
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
				// if password matches, set and send {"broadcaster_id"} to all.
				// else 'incorrect-password"
				if (item.password.equals(message.getLivePassword())) {
					BROADCASTERS_ID.put(slug, message.getPeerId());
					sendToAllInclude(slug, new BroadcasterId(message.getPeerId()));
				} else {
					sendAsync(session, new SendMessage("incorrect-password"));
				}
			}, failure -> {});
			
			return Uni.createFrom().nullItem();
		});
	}

	public void setLiveStatus(Session session, String slug, ReceivedMessage message) throws Throwable {
		VertxContextSupport.subscribeAndAwait(() -> {
			Panache.withSession(() -> Live.findBySlug(slug)).subscribe().with(item -> {
				// if password matches, set and send "live-finished" to all.
				// else 'incorrect-password"
				if (item.password.equals(message.getLivePassword())) {
					executorService.submit(() -> {
						try { changeLiveStatus(slug); } catch (Throwable e) {}
					});
					sendToAllInclude(slug, new SendMessage("live-finished"));
				} else {
					sendAsync(session, new SendMessage("incorrect-password"));
				}
			}, failure -> {});
			
			return Uni.createFrom().nullItem();
		});
	}

	private void changeLiveStatus(String slug) throws Throwable   {
		VertxContextSupport.subscribeAndAwait(() -> 
			Panache.withTransaction(() -> Live.findBySlug(slug).onItem().ifNotNull()
				.invoke(live -> live.setStatus(LiveStatus.DONE)))
		);
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
