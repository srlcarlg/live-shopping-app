package live.chat.websocket.database;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveCassandraRepository<ChatMessage, UUID> {

	@AllowFiltering
	Flux<ChatMessage> findByLiveSlug(String liveSlug);
	
	@Query("SELECT * FROM messages WHERE liveSlug = ?0 ORDER BY createAt DESC LIMIT 30")
	Flux<ChatMessage> getLastMessagesByLiveSlug(String liveSlug);
	
}