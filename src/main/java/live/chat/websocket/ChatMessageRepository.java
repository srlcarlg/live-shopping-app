package live.chat.websocket;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveCassandraRepository<ChatMessage, UUID> {

	@AllowFiltering
	Flux<ChatMessage> findByLiveSlug(String liveSlug);
}