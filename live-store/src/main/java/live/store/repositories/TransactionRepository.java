package live.store.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import live.store.entities.Transaction;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
	Flux<Transaction> findByLiveSlug(String liveSlug);
	@Query("SELECT * FROM transaction ORDER BY created_at DESC LIMIT 1;")
	Flux<Transaction> getLastest();
}
