package live.manager;

import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import live.manager.entities.Live;

@GrpcService
public class LiveGrpcService implements LiveService {

	@Override
	@WithSession
	public Uni<LiveResponse> findOneBySlug(SlugRequest request) {
		return Live.findBySlug(request.getSlug()).onItem().ifNotNull().transform(
	        	entity ->  LiveResponse.newBuilder()
	        		.setSlug(entity.slug)
	        		.setTitle(entity.title)
	        		.setDescription(entity.description)
	        		.setStatus(entity.status.toString())
	        		.setCreatedAt(entity.createdAt.toString()).build()		        		
	        	)
		        .onItem().ifNull().continueWith(LiveResponse.getDefaultInstance());
	}

	@Override
	@WithSession
	public Uni<ValidateResponse> validate(ValidateRequest request) {
		return Live.findBySlug(request.getSlug()).onItem().ifNotNull()
				.transform(entity -> 
					ValidateResponse.newBuilder()
					.setIsValid(entity.password.contentEquals(request.getPassword())).build()
				)
				.onItem().ifNull().continueWith(ValidateResponse.getDefaultInstance());
	}
}
