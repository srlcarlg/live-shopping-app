package live.store.entities;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.data.annotation.Id;

import live.store.dtos.ProductDTO;

public class Product {
	
	@Id
    private Long id;
    private String liveSlug;
    private String title;
    private Double price;
    private Integer quantity;
    private Integer timeLeft;
    private String imageUrl;
    private Timestamp createdAt;
    
    public Product() {
        this.liveSlug = "";
        this.title = "";
        this.price = 0.0;
        this.quantity = 0;
        this.timeLeft = 0;
        this.imageUrl = "";
        this.createdAt = null;
	}
    public Product(String liveSlug, String title, Double price, Integer quantity, Integer timeLeft, String imageUrl, Timestamp createdAt) {
        this.liveSlug = liveSlug;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.timeLeft = timeLeft;
        this.imageUrl = imageUrl;
        this.createdAt = Timestamp.from(Instant.now());
    }
    public Product(String liveSlug) {
        this.liveSlug = liveSlug;
        this.createdAt = Timestamp.from(Instant.now());
    }
    
    public void setFromDTO(ProductDTO dto) {
    	this.setTitle(dto.getTitle() != null ? dto.getTitle() : this.getTitle());
		this.setPrice(dto.getPrice()!= null	? dto.getPrice() : this.getPrice());
		this.setQuantity(dto.getQuantity()!= null ? dto.getQuantity() : this.getQuantity());
		this.setTimeLeft(dto.getTimeLeft() != null ? dto.getTimeLeft() : this.getTimeLeft());
		this.setImageUrl(dto.getImageUrl()!= null ? dto.getImageUrl() : this.getImageUrl());
    }
    public Product(boolean is) {
        this.liveSlug = "";
        this.title = "";
        this.price = 0.0;
        this.quantity = 0;
        this.timeLeft = 0;
        this.imageUrl = "";
        this.createdAt = null;
	}
	public Long getId() {
		return id;
	}
	public String getLiveSlug() {
		return liveSlug;
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
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setLiveSlug(String liveSlug) {
		this.liveSlug = liveSlug;
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
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", liveSlug=" + liveSlug + ", title=" + title + ", price=" + price + ", quantity="
				+ quantity + ", timeLeft=" + timeLeft + ", imageUrl=" + imageUrl + ", createdAt=" + createdAt + "]";
	}
}

