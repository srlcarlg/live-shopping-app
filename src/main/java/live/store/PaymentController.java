package live.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import live.store.dtos.PaymentDTO;
import live.store.dtos.TransactionDTO;
import live.store.entities.Product;
import live.store.entities.Transaction;
import live.store.repositories.ProductRepository;
import live.store.repositories.TransactionRepository;
import live.store.services.TransactionsService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/store")
public class PaymentController {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private TransactionRepository transactionRepository;	
	@Autowired
	private TransactionsService transactionsService;

	@PostMapping("{liveSlug}/payment")
	public Mono<Object> payment(@PathVariable String liveSlug, @RequestBody PaymentDTO payment) {

		if (payment.getProductId() == null) {
			return Mono.just("ProductId must not be null");
		}

		return productRepository.findById(payment.getProductId()).defaultIfEmpty(new Product()).flatMap(product -> {
			if (product.getId() == null) {
				return Mono.just("product-not-found");
			}
			if (!product.getLiveSlug().equals(liveSlug)) {
				return Mono.just("slug-unathorized");
			}

			// Simulate/create transaction
			Double price = payment.getQuantity() * product.getPrice();
			Transaction transaction = new Transaction(liveSlug);
			transaction.setProductId(payment.getProductId());
			transaction.setCreditCardNumber(payment.getNumber());
			transaction.setAmount(price);

			// Save then update live statement
			Mono<Transaction> transactionMono = transactionRepository.save(transaction);
			transactionsService.calculateTotalToStatement(liveSlug, price);

			return transactionMono.map(x -> new TransactionDTO(x.getUuid()));
		});
	}
}
