package live.chat.grpc;

import org.springframework.stereotype.Service;

import live.chat.LiveResponse;
import live.chat.ReactorLiveServiceGrpc.ReactorLiveServiceStub;
import live.chat.SlugRequest;
import live.chat.ValidateRequest;
import live.chat.ValidateResponse;
import reactor.core.publisher.Mono;

@Service
public class LiveGrpcService {

	ReactorLiveServiceStub stub;
	
	public LiveGrpcService(ReactorLiveServiceStub reactorStub) { 
		stub = reactorStub; 
	}
	
    public Mono<LiveResponse> findBySlug(String slug) {
    	Mono<SlugRequest> request = Mono.just(
    			SlugRequest.newBuilder().setSlug(slug).build());
		 return stub.findOneBySlug(request);
    }
    
    public Mono<ValidateResponse> validate(String slug, String password) {
    	Mono<ValidateRequest> request = Mono.just(
    			ValidateRequest.newBuilder()
    			.setSlug(slug).setPassword(password).build());
    	return stub.validate(request);
    }
}
