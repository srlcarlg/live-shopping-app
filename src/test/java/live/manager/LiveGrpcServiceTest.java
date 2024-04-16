package live.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;

@QuarkusTest
public class LiveGrpcServiceTest {
    

    @InjectMock
    @GrpcClient("something")
    LiveGrpc client; 
    
	LiveResponse liveResponse = LiveResponse.newBuilder()
	        .setSlug("slug1")
			.setTitle("Title").setDescription("Description")
			.setStatus(LiveStatus.AVAILABLE.toString()).setCreatedAt(LocalDateTime.now().toString()).build();	
	
	@BeforeEach
	void before() {
        PanacheMock.mock(Live.class);
	}
	
    @Test
    public void testFindOneBySlug() {
        Mockito.when(client.findOneBySlug(Mockito.any(SlugRequest.class)))
    		.thenReturn(Uni.createFrom().item(liveResponse));

        SlugRequest request = SlugRequest.newBuilder().setSlug("slug1").build();
        LiveResponse response = client.findOneBySlug(request).await().indefinitely();
        
        Assertions.assertEquals(liveResponse.toString(), response.toString());
    }

    @Test
    public void testLogin() {	        		
        // Correct password
        LoginRequest request = LoginRequest.newBuilder()
            .setSlug("slug1")
            .setPassword("correctPassword")
            .build();
        
        Mockito.when(client.login(request))
			.thenReturn(Uni.createFrom().item(liveResponse));
        
        LiveResponse response = client.login(request).await().indefinitely();

        assertEquals(liveResponse.toString(), response.toString());
        
        // Wrong password
        LoginRequest requestWrong = LoginRequest.newBuilder()
            .setSlug("slug1")
            .setPassword("wrongPassword")
            .build();

        Mockito.when(client.login(requestWrong))
			.thenReturn(Uni.createFrom().item(LiveResponse.getDefaultInstance()));
        
        LiveResponse responseWrong = client.login(requestWrong).await().indefinitely();

        assertEquals(LiveResponse.getDefaultInstance().toString(), responseWrong.toString());
    }
}