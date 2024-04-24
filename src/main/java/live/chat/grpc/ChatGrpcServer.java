package live.chat.grpc;

import java.util.concurrent.ConcurrentHashMap;

import live.chat.grpc.chat.BroadcasterResponse;
import live.chat.grpc.chat.ReactorChatServiceGrpc;
import live.chat.grpc.chat.SlugRequest;
import live.chat.grpc.chat.ValidateResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class ChatGrpcServer extends ReactorChatServiceGrpc.ChatServiceImplBase {
	
	public static ConcurrentHashMap<String, String> broadcastersIDs = new ConcurrentHashMap<>();

	@Override
	public Mono<BroadcasterResponse> getBroadcaster(Mono<SlugRequest> request) {
		return Mono.just(
				BroadcasterResponse.newBuilder()
                .setSessionId("Hello ==> " + "REACTOR")
                .build());
	}
	
	@Override
	public Mono<ValidateResponse> validateSlug(Mono<SlugRequest> request) {
		return Mono.just(
				ValidateResponse.newBuilder()
                .setIsValid(true)
                .build());
	}
}