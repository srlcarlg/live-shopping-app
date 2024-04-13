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
		return Live.findBySlug(request.getSlug())
		        .onItem().ifNotNull()
		        	.transform(entity ->  LiveResponse.newBuilder()
		        		.setSlug(entity.getSlug())
		        		.setTitle(entity.getTitle()).setDescription(entity.getDescription())
		        		.setStatus(entity.getStatus().toString()).setCreatedAt(entity.getCreatedAt().toString()).build()		        		
		        	)
		        .onItem().ifNull()
		        	.continueWith(LiveResponse.getDefaultInstance());
	}

	@Override
	@WithSession
	public Uni<LiveResponse> login(LoginRequest request) {
		return Live.findBySlug(request.getSlug())
		        .onItem().ifNotNull()
		        	.transform(
		        		entity -> entity.getPassword().contentEquals(request.getPassword()) 
        				?	LiveResponse.newBuilder()
    		        		.setSlug(entity.getSlug())
    		        		.setTitle(entity.getTitle()).setDescription(entity.getDescription())
    		        		.setStatus(entity.getStatus().toString()).setCreatedAt(entity.getCreatedAt().toString()).build()
    		        	: LiveResponse.getDefaultInstance()		        				        		
		        	)
		        .onItem().ifNull()
		        	.continueWith(LiveResponse.getDefaultInstance());
	}
}
