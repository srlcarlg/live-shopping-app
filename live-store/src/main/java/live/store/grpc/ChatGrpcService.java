package live.store.grpc;

import live.store.grpc.chat.BroadcasterResponse;
import live.store.grpc.chat.ReactorChatServiceGrpc.ReactorChatServiceStub;
import live.store.grpc.chat.SlugRequest;
import live.store.grpc.chat.ValidateResponse;
import reactor.core.publisher.Mono;

public class ChatGrpcService {

	ReactorChatServiceStub stub;

	public ChatGrpcService(ReactorChatServiceStub reactorStub) {
		stub = reactorStub;
	}

	public Mono<BroadcasterResponse> getBroadcaster(String slug) {
		return stub.getBroadcaster(Mono.just(SlugRequest.newBuilder().setSlug(slug).build()));
	}

	public Mono<ValidateResponse> validateSlug(String slug) {
		return stub.validateSlug(Mono.just(SlugRequest.newBuilder().setSlug(slug).build()));
	}
}
