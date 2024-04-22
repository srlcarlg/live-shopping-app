package live.store.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import live.store.entities.LiveStatement;
import reactor.core.publisher.Flux;

public interface LiveStatementRepository extends ReactiveCrudRepository<LiveStatement, Long> {
	Flux<LiveStatement> findByLiveSlug(String liveSlug);
}
