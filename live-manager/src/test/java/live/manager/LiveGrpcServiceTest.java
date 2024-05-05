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
    LiveService client; 
        
	@BeforeEach
	void before() {
        PanacheMock.mock(Live.class);
	}
	
    @Test
    public void testFindOneBySlug() {
    	LiveResponse liveResponse = LiveResponse.newBuilder()
    	        .setSlug("slug1")
    			.setTitle("Title").setDescription("Description")
    			.setStatus(LiveStatus.AVAILABLE.toString())
    			.setCreatedAt(LocalDateTime.now().toString())
    			.build();	
    	
        Mockito.when(client.findOneBySlug(Mockito.any(SlugRequest.class)))
    		.thenReturn(Uni.createFrom().item(liveResponse));

        SlugRequest request = SlugRequest.newBuilder().setSlug("slug1").build();
        LiveResponse response = client.findOneBySlug(request).await().indefinitely();
        
        Assertions.assertEquals(liveResponse.toString(), response.toString());
    }

    @Test
    public void testValidate() {	    
    	
        // Correct password
        ValidateRequest request = ValidateRequest.newBuilder()
            .setSlug("slug1")
            .setPassword("correctPassword")
            .build();
        ValidateResponse validResponse = ValidateResponse.newBuilder()
            .setIsValid(true)
            .build();
        
        Mockito.when(client.validate(request))
			.thenReturn(Uni.createFrom().item(validResponse));
        
        ValidateResponse response = client.validate(request).await().indefinitely();

        assertEquals(validResponse.toString(), response.toString());
        
        // Wrong password or slug not found = empty json
        request = ValidateRequest.newBuilder()
            .setSlug("slug1")
            .setPassword("wrongPassword")
            .build();
        validResponse = ValidateResponse.getDefaultInstance();
        
        Mockito.when(client.validate(request))
			.thenReturn(Uni.createFrom().item(validResponse));
        
        response = client.validate(request).await().indefinitely();

        assertEquals(validResponse.toString(), response.toString());
        
        // Wrong slug
        request = ValidateRequest.newBuilder()
            .setSlug("WrongSlug")
            .setPassword("pass")
            .build();
        
        Mockito.when(client.validate(request))
			.thenReturn(Uni.createFrom().item(validResponse));
        
        response = client.validate(request).await().indefinitely();

        assertEquals(ValidateResponse.getDefaultInstance().toString(), response.toString());
    }
}