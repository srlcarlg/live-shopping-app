package live.manager;

import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import live.manager.entities.Live;

@GrpcService
public class LiveGrpcService implements LiveGrpc {

	@Override
	@WithSession
	public Uni<LiveResponse> findOneBySlug(SlugRequest request) {
		return Live.findBySlug(request.getSlug()).onItem().ifNotNull().transform(
	        	entity ->  LiveResponse.newBuilder()
	        		.setSlug(entity.slug)
	        		.setTitle(entity.title).setDescription(entity.description)
	        		.setStatus(entity.status.toString())
	        		.setCreatedAt(entity.createdAt.toString()).build()		        		
	        	)
		        .onItem().ifNull()
		        .continueWith(LiveResponse.getDefaultInstance());
	}

	@Override
	@WithSession
	public Uni<LiveResponse> login(LoginRequest request) {
		return Live.findBySlug(request.getSlug()).onItem().ifNotNull().transform(
		        entity -> entity.password.contentEquals(request.getPassword()) 
    				?	LiveResponse.newBuilder()
		        		.setSlug(entity.slug)
		        		.setTitle(entity.title).setDescription(entity.description)
		        		.setStatus(entity.status.toString())
		        		.setCreatedAt(entity.createdAt.toString()).build()
		        	: LiveResponse.getDefaultInstance()		        				        		
	        	)
		        .onItem().ifNull()
		        .continueWith(LiveResponse.getDefaultInstance());
	}
}
