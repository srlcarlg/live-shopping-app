package live.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import live.store.dtos.ProductDTO;
import live.store.entities.Product;
import live.store.grpc.ChatGrpcService;
import live.store.grpc.chat.BroadcasterResponse;
import live.store.repositories.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/store")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ChatGrpcService chatGrpcService;

	@GetMapping("/all")
	public Flux<Product> getAll() {
		return productRepository.findAll();
	}
	
	@GetMapping("/{liveSlug}")
	public Flux<Product> getAllBySlug(@PathVariable String liveSlug) {
		return productRepository.findByLiveSlug(liveSlug);
	}

	@PostMapping("/{liveSlug}")
	public Mono<Object> createProduct(@PathVariable String liveSlug, @RequestBody ProductDTO dto) {
		if (dto.getSessionId() == null) {
			return Mono.just("sessionId must be not null");
		}
		return chatGrpcService.getBroadcaster(liveSlug).flatMap(response -> {
            if (response.equals(BroadcasterResponse.getDefaultInstance())) {
            	return Mono.just("broadcaster-not-found");
            }
            if (response.getSessionId().equals(dto.getSessionId())) {
            	Product toInsert = new Product(liveSlug);
        		toInsert.setFromDTO(dto);
        		return productRepository.save(toInsert);
            } else {
            	return Mono.just("session-unauthorized");
            }
        });
	}

	@PutMapping("/{liveSlug}/{id}")
	public Mono<Object> updateProduct(@PathVariable String liveSlug, @PathVariable Long id, @RequestBody ProductDTO dto) {
		return productRepository.findById(id).flatMap(existingProduct -> {
			if (!existingProduct.getLiveSlug().equals(liveSlug)) {
				return Mono.just("slug-unathorized");
			}
			return chatGrpcService.getBroadcaster(liveSlug).flatMap(response -> {
	            if (response.getSessionId().equals(dto.getSessionId())) {
	    			existingProduct.setFromDTO(dto);
	    			return productRepository.save(existingProduct);
	            } else {
	            	return Mono.just("session-unauthorized");
	            }
	        });
		});
	}

	@DeleteMapping("/{liveSlug}/{sessionId}/{id}")
	public Mono<Object> deleteProduct(@PathVariable String liveSlug, @PathVariable String sessionId, @PathVariable Long id) {
		return productRepository.findById(id).flatMap(existingProduct -> {
			if (!existingProduct.getLiveSlug().equals(liveSlug)) {
				return Mono.just("slug-unathorized");
			}
			return chatGrpcService.getBroadcaster(liveSlug).flatMap(response -> {
	            if (response.getSessionId().equals(sessionId)) {
	    			return productRepository.deleteById(id);
	            } else {
	            	return Mono.just("session-unauthorized");
	            }
	        });
		});
	}
	
}
