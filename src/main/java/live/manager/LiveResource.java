package live.manager;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import live.manager.entities.Live;
import live.manager.entities.LiveStatus;
import live.manager.utils.RandomGenerator;

@Path("/lives")
public class LiveResource {
	
	@Inject 
	RandomGenerator randomGenerator;
	
    @GET
    public Uni<List<Live>> getAll() {
        return Live.listAll(Sort.by("title"));
    }

    @GET
    @Path("/{slug}")
    public Uni<Live> findOneBySlug(String slug) {
        return Live.findBySlug(slug);
    }
    
    @POST
    public Uni<Response> create(Live liveRequest) {
		Live live = new Live(
			randomGenerator.randomString(14),
			liveRequest.getTitle(), liveRequest.getDescription(), liveRequest.getPassword(),
			LiveStatus.AVAILABLE, LocalDateTime.now()
		);
		return Panache.withTransaction(live::persist)
                .replaceWith(Response.ok(live).status(Status.CREATED)::build);
	}
    
    @Path("/{slug}")
    @PUT
    public Uni<Response> edit(String slug, Live liveRequest) {
		return Live.findBySlug(slug)
		.onItem().ifNotNull()
				.invoke(live -> live.setPassword(liveRequest.getPassword()))
        .onItem().ifNotNull()
        	.transform(entity -> Response.ok(entity).build())
        .onItem().ifNull()
        	.continueWith(Response.ok().status(Status.NOT_FOUND)::build);

	}
}
