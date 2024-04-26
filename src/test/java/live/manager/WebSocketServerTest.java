package live.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;
import live.manager.utils.SharedExecutor;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class WebSocketServerTest {

	@TestHTTPResource("/live")
	String WS_URI;
	
	private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();
	
	@BeforeEach
	void beforeEach() {
		// testRooms + testOnOpenValidations
        PanacheMock.mock(Live.class);
		MESSAGES.clear();
		
		// testOnMessage
		// Changed from Thread.startVirtualThread() to ExecutorService
		// in order to share the mocked objects to all threads for TESTING ONLY
		SharedExecutor sharedExecutor = new SharedExecutor(WebSocketServer.executorService);
		sharedExecutor.execute(() -> {
            PanacheMock.mock(Live.class);
            Mockito.when(Live.findBySlug("randomSlug")).thenReturn(Uni.createFrom().item(
            	new Live("randomSlug", "Title", "Desc", "Pass", LiveStatus.AVAILABLE))
            );
		});
	}
	
	@Test
	@DisplayName("Each Room should have nº sessions")
	public void testRooms() throws Exception {
        when(Live.findBySlug("room1")).thenReturn(Uni.createFrom().item(
            new Live("room1", "Title", "Desc", "Pass", LiveStatus.AVAILABLE))
        );
        when(Live.findBySlug("room2")).thenReturn(Uni.createFrom().item(
            new Live("room2", "Title", "Desc", "Pass", LiveStatus.AVAILABLE))
        );
        
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		List<String> slugs = Arrays.asList("/room1", "/room1", "/room1", "/room2");
		
		slugs.stream().forEach(slug -> { 
			try {
				container.connectToServer(new Client(), new URI(WS_URI + slug));
			} catch (DeploymentException | IOException | URISyntaxException e) {}
		});
        
		try { Thread.sleep(4000); } catch (Exception e) {}
		assertEquals(3, WebSocketServer.ROOMS.get("room1").size());
		assertEquals(1, WebSocketServer.ROOMS.get("room2").size());
	}

	@Test
	@DisplayName("Expected onOpen behavior")
	@SuppressWarnings("static-access")
	public void testOnOpenValidations() throws Exception {
		// OnOpen should sends 'live-finished'/'live-finished' texts 
		// after visits database, then closes session
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		
		when(Live.findBySlug("slug")).thenReturn(Uni.createFrom().nullItem());        
        Session session2 = container.connectToServer(new Client(), new URI(WS_URI + "/slug"));
        Assertions.assertEquals("slug-not-found", MESSAGES.poll(10, TimeUnit.SECONDS));
        
        when(Live.findBySlug("12345")).thenReturn(Uni.createFrom().item(
        	new Live("12345", "Title", "Desc", "Pass", LiveStatus.DONE))
        );
        Session session = container.connectToServer(new Client(), new URI(WS_URI + "/12345"));
        Assertions.assertEquals("live-finished", MESSAGES.poll(10, TimeUnit.SECONDS));
        
        try { Thread.sleep(200); } catch (Exception e) {}
        
        Assertions.assertTrue(!session.isOpen());
        Assertions.assertTrue(!session2.isOpen());

        PanacheMock.verify(Live.class, Mockito.atMost(1)).findBySlug("slug");
        PanacheMock.verify(Live.class, Mockito.atMost(1)).findBySlug("12345");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"join-text", "SetBroadcaster-json", "SetLiveStatus-json"})
	@DisplayName("Expected behavior of each onMessage types")
	@SuppressWarnings("static-access")
    public void testOnMessage(String messageMethod) throws Exception {
		
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ObjectMapper mapper = new ObjectMapper();
		MESSAGES.clear();
		
		switch (messageMethod) {
		case "join-text": {
            Map<String, Integer> dataUsers = new HashMap<>();
			
            //  sends {users_count: int} to all sessions except current.
            dataUsers.put("users_count", 4);
			for (int i = 1; i <= 4; i++) {
		        try { Thread.sleep(50); } catch (Exception e) {}
				Session s = container.connectToServer(new Client(), new URI(WS_URI + "/randomSlug"));
				if (i == 4) {
					
					try { Thread.sleep(50); } catch (Exception e) {}
					MESSAGES.clear();
					s.getAsyncRemote().sendText("join");
					Assertions.assertEquals(mapper.writeValueAsString(dataUsers), MESSAGES.pollLast(10, TimeUnit.SECONDS));
			        
					try { Thread.sleep(50); } catch (Exception e) {}
					MESSAGES.clear();
					s.close();
		            dataUsers.put("users_count", 3);
					Assertions.assertEquals(mapper.writeValueAsString(dataUsers), MESSAGES.pollLast(10, TimeUnit.SECONDS));
		        }
			};
			
			break;
		}
		case "SetBroadcaster-json": {
            Map<String, String> data = new HashMap<>();
            data.put("peer_id", "123random");
            data.put("live_password", "PassWrong");

            Session session = container.connectToServer(new Client(), new URI(WS_URI + "/randomSlug"));
           
            // Server sends "incorrect-password" text if it doesn't match.
            String json = mapper.writeValueAsString(data);
	        session.getAsyncRemote().sendText(json);
	        
	        Assertions.assertEquals("incorrect-password", MESSAGES.poll(10, TimeUnit.SECONDS));
            
	        // Server sends "broadcaster-id" if it matches
            data.put("live_password", "Pass");
            json = mapper.writeValueAsString(data);
	        session.getAsyncRemote().sendText(json);
	        
	        data.clear();
	        data.put("broadcaster_id", "123random");
	        Assertions.assertEquals(mapper.writeValueAsString(data), MESSAGES.poll(10, TimeUnit.SECONDS));
	        
	        // 1 onOpen, 2 to verify password
	        PanacheMock.verify(Live.class, Mockito.atMost(3)).findBySlug("randomSlug");
	        
			break;
		}
		case "SetLiveStatus-json": {
			
            Map<String, String> data = new HashMap<>();
            data.put("live_password", "PassWrong");

            Session session = container.connectToServer(new Client(), new URI(WS_URI + "/randomSlug"));
           
            // Server sends "incorrect-password" text if it doesn't match.
            String json = mapper.writeValueAsString(data);
	        session.getAsyncRemote().sendText(json);
	        
	        Assertions.assertEquals("incorrect-password", MESSAGES.poll(10, TimeUnit.SECONDS));

	        // Server sends "live-finished" for others sessions (except itself) if password matches            
	        data.put("live_password", "Pass");
            json = mapper.writeValueAsString(data);

	        // another session
            //container.connectToServer(new Client(), new URI(WS_URI + "/randomSlug"));
   
	        session.getAsyncRemote().sendText(json);        
	        Assertions.assertEquals("live-finished", MESSAGES.poll(10, TimeUnit.SECONDS));

	        // 2 onOpen, 2 for password
	        // 1 Can't count sub-thread with Panache.withTransaction()
	        PanacheMock.verify(Live.class, Mockito.atMost(4)).findBySlug("randomSlug");
	        
			break;
		}
		}
	}
	
	@ClientEndpoint
	public static class Client {
        @OnMessage
        public void onMessage(String message) {
        	MESSAGES.add(message);
        }
	}
}