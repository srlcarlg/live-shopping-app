package live.store.dtos;

public class PaymentDTO {
	
	private Long productId;
	private Integer quantity;
	private String name;
	private Long number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;
	
	public PaymentDTO(Long productId, Integer quantity, String name, Long number, Integer expirationMonth,
			Integer expirationYear, Integer cVV) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.name = name;
		this.number = number;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		CVV = cVV;
	}
	
	public Long getProductId() {
		return productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public String getName() {
		return name;
	}
	public Long getNumber() {
		return number;
	}
	public Integer getExpirationMonth() {
		return expirationMonth;
	}
	public Integer getExpirationYear() {
		return expirationYear;
	}
	public Integer getCVV() {
		return CVV;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}
	public void setCVV(Integer cVV) {
		CVV = cVV;
	}
}
