package live.manager;

import java.util.List;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
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
        return Live.getAllLives();
    }

    @GET
    @Path("/{slug}")
    public Uni<Live> findOneBySlug(String slug) {
        return Live.findBySlug(slug);
    }

    @POST
    @WithTransaction
    public Uni<Response> create(Live liveRequest) {
		Live live = new Live(
			randomGenerator.randomString(14),
			liveRequest.getTitle(), liveRequest.getDescription(), liveRequest.getPassword(),
			LiveStatus.AVAILABLE
		);
		return live.persist().replaceWith(Response.ok(live).status(Status.CREATED)::build);
	}

    @Path("/{slug}")
    @PUT
    @WithTransaction
    public Uni<Response> edit(String slug, Live liveRequest) {
		return Live.findBySlug(slug).onItem().ifNotNull()
			.invoke(live -> live.setPassword(liveRequest.getPassword()))
        .onItem().ifNotNull()
        	.transform(entity -> Response.ok(entity).build())
        .onItem().ifNull()
        	.continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}
}
