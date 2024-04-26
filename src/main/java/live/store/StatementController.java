package live.store;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import live.store.entities.LiveStatement;
import live.store.entities.Transaction;
import live.store.repositories.LiveStatementRepository;
import live.store.repositories.TransactionRepository;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/store")
public class StatementController {

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private LiveStatementRepository statementRepository;

	@GetMapping(path = "/transactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Transaction> getLatestTransactions() {
		return Flux.interval(Duration.ofSeconds(4)).flatMap(x -> {
			return transactionRepository.getLastest();
		});
	}
	
	@GetMapping(path = "/total", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<StatementTotal> getTotal() {
		return Flux.interval(Duration.ofSeconds(4)).flatMap(y -> {
			return statementRepository.findAll().flatMap(
					live -> transactionRepository.findByLiveSlug(live.getLiveSlug())
					.count().map(x -> new StatementTotal(live, x)));
		});
	}

	@SuppressWarnings("unused")
	private static class StatementTotal {
		private final String liveSlug;
		private final Double total;
		private final Long nTransactions;

		public StatementTotal(LiveStatement entity, Long nTransactions) {
			super();
			this.liveSlug = entity.getLiveSlug();
			this.total = entity.getTotal();
			this.nTransactions = nTransactions;
		}
		
		public String getLiveSlug() {
			return liveSlug;
		}
		public Double getTotal() {
			return total;
		}
		public Long getnTransactions() {
			return nTransactions;
		}		
	}
}
