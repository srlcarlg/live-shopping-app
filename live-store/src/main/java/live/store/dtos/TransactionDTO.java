package live.store.dtos;

import java.util.UUID;

public class TransactionDTO {
	
	UUID transactionId;
	
	public TransactionDTO(UUID uuid) {
		this.transactionId = uuid;
	}
	
	public UUID getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}
}
