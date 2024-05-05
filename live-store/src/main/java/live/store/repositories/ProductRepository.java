package live.store.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import live.store.entities.Product;
import reactor.core.publisher.Flux;


public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
	Flux<Product> findByLiveSlug(String liveSlug);
}
