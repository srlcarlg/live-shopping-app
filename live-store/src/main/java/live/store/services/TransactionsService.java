package live.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import live.store.entities.LiveStatement;
import live.store.entities.Transaction;
import live.store.repositories.LiveStatementRepository;
import live.store.repositories.TransactionRepository;

@Service
public class TransactionsService {

	@Autowired
	private LiveStatementRepository statementRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	
	public void calculateTotalToStatement(String liveSlug, Double firstTotal) {
		transactionRepository.findByLiveSlug(liveSlug).collectList()
		.doOnNext(transactions -> {
			double total = transactions.parallelStream()
					.mapToDouble(Transaction::getAmount)
					.sum();
			statementRepository.findByLiveSlug(liveSlug)
			.defaultIfEmpty(new LiveStatement(liveSlug))
			.flatMap(x -> {
				x.setTotal(total == 0 ? firstTotal : total);
				return statementRepository.save(x);
			}).subscribe();
		}).subscribe();
	}
}
