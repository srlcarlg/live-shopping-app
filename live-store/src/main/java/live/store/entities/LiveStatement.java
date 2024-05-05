package live.store.entities;

import org.springframework.data.annotation.Id;

public class LiveStatement {
	
    @Id
    private Long id;
    private String liveSlug;
    private Double total;
    
    public LiveStatement() {}
    
	public LiveStatement(String liveSlug) {
		super();
		this.liveSlug = liveSlug;
		this.total = 0.0;
	}
	
	public Long getId() {
		return id;
	}
	public String getLiveSlug() {
		return liveSlug;
	}
	public double getTotal() {
		return total;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setLiveSlug(String liveSlug) {
		this.liveSlug = liveSlug;
	}
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "LiveStatement [id=" + id + ", liveSlug=" + liveSlug + ", total=" + total + "]";
	}
}
