package live.manager;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;

@QuarkusTest
@TestHTTPEndpoint(LiveResource.class) 
class LiveResourceTest {
    
	Live live = new Live("randomSlug", "Title", "Description", "Pass", LiveStatus.AVAILABLE);

	@BeforeEach
	void before() {
        PanacheMock.mock(Live.class);
	}
	
	@Test
	public void testGetAll() {        
	    List<Live> lives = List.of(live);
	    Mockito.when(Live.getAllLives()).thenReturn(Uni.createFrom().item(lives));
	    given()
	        .when().get()
	        .then()
	        .statusCode(200)
	        .body("$.size()", is(1),
	              "[0].title", is("Title"));
	}
	
	@Test
	public void testFindOneBySlug() {        
	    Mockito.when(Live.findBySlug("randomSlug")).thenReturn(Uni.createFrom().item(live));
	    given()
	        .when().get("/{slug}", "randomSlug")
	        .then()
	        .statusCode(200)
	        .body("title", is("Title"));
	}
	
	@Test
	public void testCreate() {        
	    Mockito.when(Live.persist(live)).thenReturn(Uni.createFrom().nullItem());
	    given()
	        .contentType("application/json")
	        .body(live)
	        .when().post()
	        .then()
	        .statusCode(201)
	        .body("title", is("Title"));
	}

	@Test
	public void testEdit() {
	    Live existingLive = new Live("slugToEdit", "OldTitle", "OldDesc", "OldPass", LiveStatus.AVAILABLE);
	    
	    Mockito.when(Live.findBySlug("slugToEdit")).thenReturn(Uni.createFrom().item(existingLive));
	    Mockito.when(Live.persist(existingLive)).thenReturn(Uni.createFrom().nullItem());

	    Live updatedLive = new Live("slugToEdit", "OldTitle", "OldDesc", "NewPass", LiveStatus.AVAILABLE);

	    given()
	        .contentType("application/json")
	        .body(updatedLive)
	        .when().put("/{slug}", "slugToEdit")
	        .then()
	        .statusCode(200)
	        .body("password", is("NewPass"));
	}

}