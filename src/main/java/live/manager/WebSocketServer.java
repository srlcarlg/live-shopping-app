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

@ServerEndpoint("/live/{slug}")
@ApplicationScoped
public class WebSocketServer {
	
	public static Map<String, ConcurrentLinkedDeque<Session>> ROOMS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("slug") String slug) {
        System.out.println("onOpen> " + slug);
        
        ROOMS.computeIfAbsent(slug, s -> new ConcurrentLinkedDeque<>());
        ROOMS.get(slug).add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("slug") String slug) {
        System.out.println("onClose> " + slug);
        
        ROOMS.get(slug).remove(session);
    }

    @OnError
    public void onError(Session session, @PathParam("slug") String slug, Throwable throwable) {
        System.out.println("onError> " + slug + ": " + throwable);

        System.out.println(ROOMS.get(slug).size());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("slug") String slug) {
        System.out.println("onMessage> " + slug + ": " + message);
        
        System.out.println(ROOMS.get(slug).size());
    }
}
