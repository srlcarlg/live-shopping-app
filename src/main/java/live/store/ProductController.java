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
import live.store.repositories.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/store")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/all")
	public Flux<Product> getAll() {
		return productRepository.findAll();
	}
	
	@GetMapping("/{liveSlug}")
	public Flux<Product> getAllBySlug(@PathVariable String liveSlug) {
		return productRepository.findByLiveSlug(liveSlug);
	}

	@PostMapping("/{liveSlug}")
	public Mono<Product> createProduct(@PathVariable String liveSlug, @RequestBody ProductDTO dto) {
		 // TODO: Validate Slug and Broadcaster ID
		Product toInsert = new Product(liveSlug);
		toInsert.setFromDTO(dto);
		return productRepository.save(toInsert);
	}

	@PutMapping("/{liveSlug}/{id}")
	public Mono<Object> updateProduct(@PathVariable String liveSlug, @PathVariable Long id, @RequestBody ProductDTO dto) {
		return productRepository.findById(id).flatMap(existingProduct -> {
			if (!existingProduct.getLiveSlug().equals(liveSlug)) {
				return Mono.just("slug-unathorized");
			}
			existingProduct.setFromDTO(dto);
			return productRepository.save(existingProduct);
		});
	}

	@DeleteMapping("/{liveSlug}/{id}")
	public Mono<Object> deleteProduct(@PathVariable String liveSlug, @PathVariable Long id) {
		return productRepository.findById(id).flatMap(existingProduct -> {
			if (!existingProduct.getLiveSlug().equals(liveSlug)) {
				return Mono.just("slug-unathorized");
			}
			return productRepository.deleteById(id);
		});
	}
	
}
