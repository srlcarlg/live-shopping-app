package live.chat.grpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import live.chat.grpc.chat.BroadcasterResponse;
import live.chat.grpc.chat.ReactorChatServiceGrpc;
import live.chat.grpc.chat.SlugRequest;
import live.chat.grpc.chat.ValidateResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class ChatGrpcServer extends ReactorChatServiceGrpc.ChatServiceImplBase {
	
	public static Map<String, String> broadcastersIDs = new ConcurrentHashMap<>();
	public static ConcurrentLinkedDeque<String> slugs = new ConcurrentLinkedDeque<>();

	@Override
	public Mono<BroadcasterResponse> getBroadcaster(Mono<SlugRequest> request) {
		return request.map(SlugRequest::getSlug).flatMap(slug -> {
			try {
				String sessionId = broadcastersIDs.get(slug);
				return Mono.just(BroadcasterResponse.newBuilder()
		                .setSessionId(sessionId)
		                .build());
			} catch (Exception e) {}
			return Mono.just(BroadcasterResponse.getDefaultInstance());
		});				
	}
	
	@Override
	public Mono<ValidateResponse> validateSlug(Mono<SlugRequest> request) {
		return request.map(SlugRequest::getSlug).flatMap(slug -> {
			try {
				Boolean isValid = slugs.contains(slug);
				return Mono.just(ValidateResponse.newBuilder()
		                .setIsValid(isValid)
		                .build());
			} catch (Exception e) {}
			return Mono.just(ValidateResponse.getDefaultInstance());
		});			
	}
}