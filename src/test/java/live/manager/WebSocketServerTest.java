package live.manager;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@QuarkusTest
public class WebSocketServerTest {

    @TestHTTPResource("/live")
    String WS_URI;

    @Test
    public void testSessionCounts() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(new Client(), new URI(WS_URI + "/test-room1"));
        container.connectToServer(new Client(), new URI(WS_URI + "/test-room1"));
        container.connectToServer(new Client(), new URI(WS_URI + "/test-room1"));
        
        container.connectToServer(new Client(), new URI(WS_URI + "/test-room2"));

        assertEquals(3, WebSocketServer.ROOMS.get("test-room1").size());
        assertEquals(1, WebSocketServer.ROOMS.get("test-room2").size());
    }


	@ClientEndpoint
    public static class Client {
        @OnOpen
        public void open(Session session) {
            // Send a message to indicate that we are ready,
            // as the message handler may not be registered immediately after this callback.
            session.getAsyncRemote().sendText("_ready_");
        }
    }
}