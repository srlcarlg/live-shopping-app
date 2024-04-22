package live.store.dtos;

public class ProductDTO {

	private String title;
	private Double price;
	private Integer quantity;
	private Integer timeLeft;
	private String imageUrl;
	
	public ProductDTO(String title, Double price, Integer quantity, Integer timeLeft, String imageUrl) {
		super();
		this.title = title;
		this.price = price;
		this.quantity = quantity;
		this.timeLeft = timeLeft;
		this.imageUrl = imageUrl;
	}
	
	public String getTitle() {
		return title;
	}
	public Double getPrice() {
		return price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public Integer getTimeLeft() {
		return timeLeft;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setTimeLeft(Integer timeLeft) {
		this.timeLeft = timeLeft;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
