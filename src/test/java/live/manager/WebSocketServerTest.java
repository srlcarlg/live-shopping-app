package live.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class WebSocketServerTest {

	@TestHTTPResource("/live")
	String WS_URI;
	
    private static final ConcurrentLinkedDeque<String> MESSAGES = new ConcurrentLinkedDeque<>();

	@BeforeEach
	void before() {
        PanacheMock.mock(Live.class);
		MESSAGES.clear();
	}
	
	@Test
	@DisplayName("Each Room should have nº sessions")
	public void testRooms() throws Exception {
        when(Live.findBySlug("room1")).thenReturn(Uni.createFrom().item(
            new Live("room1", "Title", "Desc", "Pass", LiveStatus.AVAILABLE, LocalDateTime.now()))
        );
        when(Live.findBySlug("room2")).thenReturn(Uni.createFrom().item(
            new Live("room2", "Title", "Desc", "Pass", LiveStatus.AVAILABLE, LocalDateTime.now()))
        );
        
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		List<String> slugs = Arrays.asList("/room1", "/room1", "/room1", "/room2");
		
		slugs.parallelStream().forEach(slug -> { 
			try {
				container.connectToServer(new Client(), new URI(WS_URI + slug));
			} catch (DeploymentException | IOException | URISyntaxException e) {}
		});
        
		try { Thread.sleep(1000); } catch (Exception e) {}
		assertEquals(3, WebSocketServer.ROOMS.get("room1").size());
		assertEquals(1, WebSocketServer.ROOMS.get("room2").size());
	}

	@Test
	@DisplayName("OnOpen should sends 'live-finished'/'live-finished' texts after visits database, then closes session")
    public void testOnOpenValidations() throws Exception {
        when(Live.findBySlug("slug")).thenReturn(Uni.createFrom().nullItem());
        when(Live.findBySlug("12345")).thenReturn(Uni.createFrom().item(
        	new Live("12345", "Title", "Desc", "Pass", LiveStatus.DONE, LocalDateTime.now()))
        );

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Session session = container.connectToServer(new Client(), new URI(WS_URI + "/12345"));
        Session session2 = container.connectToServer(new Client(), new URI(WS_URI + "/slug"));
        
        try { Thread.sleep(1000); } catch (Exception e) {}
        
        Assertions.assertTrue(MESSAGES.contains("slug-not-found"));
        Assertions.assertTrue(MESSAGES.contains("live-finished"));

        Assertions.assertTrue(!session.isOpen());
        Assertions.assertTrue(!session2.isOpen());
        
        PanacheMock.verify(Live.class);
		Live.findBySlug("slug");
        PanacheMock.verify(Live.class);
		Live.findBySlug("12345");
    }

	@ClientEndpoint
	public static class Client {
		@OnOpen
		public void open(Session session) {
			// Send a message to indicate that we are ready,
			// as the message handler may not be registered immediately after this callback.
			// session.getAsyncRemote().sendText("_ready_");
		}
        @OnMessage
        public void onMessage(String message) {
        	MESSAGES.add(message);
        	System.out.println(message);
        }
	}
}