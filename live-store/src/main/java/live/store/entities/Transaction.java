package live.store.entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;

public class Transaction {
	
    @Id
    private Long id;
    private UUID uuid;
    private String liveSlug;
    private Long productId;
    private Double amount;
    private Long creditCardNumber;
    private Timestamp createdAt;
	
    public Transaction() {}
    
    public Transaction(String liveSlug) {
		super();
		this.uuid = UUID.randomUUID();
		this.liveSlug = liveSlug;
		this.createdAt = Timestamp.from(Instant.now());
	}
	
	public Long getId() {
		return id;
	}
	public UUID getUuid() {
		return uuid;
	}
	public String getLiveSlug() {
		return liveSlug;
	}
	public Long getProductId() {
		return productId;
	}
	public Double getAmount() {
		return amount;
	}
	public Long getCreditCardNumber() {
		return creditCardNumber;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public void setLiveSlug(String liveSlug) {
		this.liveSlug = liveSlug;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setCreditCardNumber(Long creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}    
}

