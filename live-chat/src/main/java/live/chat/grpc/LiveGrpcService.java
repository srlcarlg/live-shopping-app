package live.chat.grpc;

import org.springframework.stereotype.Service;

import live.chat.grpc.live.LiveResponse;
import live.chat.grpc.live.ReactorLiveServiceGrpc.ReactorLiveServiceStub;
import live.chat.grpc.live.SlugRequest;
import live.chat.grpc.live.ValidateRequest;
import live.chat.grpc.live.ValidateResponse;
import reactor.core.publisher.Mono;

@Service
public class LiveGrpcService {

	ReactorLiveServiceStub stub;

	public LiveGrpcService(ReactorLiveServiceStub reactorStub) {
		stub = reactorStub;
	}

	public Mono<LiveResponse> findBySlug(String slug) {
		return stub.findOneBySlug(Mono.just(SlugRequest.newBuilder().setSlug(slug).build()));
	}

	public Mono<ValidateResponse> validate(String slug, String password) {
		return stub.validate(Mono.just(ValidateRequest.newBuilder()
				.setSlug(slug).setPassword(password).build()));
	}
}
