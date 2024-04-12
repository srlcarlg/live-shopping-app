package live.manager.entities;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;

@Entity
public class Live extends PanacheEntity {
    public String slug;
    public String title;
    public String description;
    public String password;
    public LiveStatus status;
    public LocalDateTime createdAt;

    public Live() {}

    public Live(String slug, String title, String description, String password, LiveStatus status,
			LocalDateTime createdAt) {
		super();
		this.slug = slug;
		this.title = title;
		this.description = description;
		this.password = password;
		this.status = status;
		this.createdAt = createdAt;
	}

	// Active record pattern 
    public static Uni<List<Live>> getAllLives() {
    	return findAll().list();
		
	}
    public static Uni<Live> findBySlug(String slug){
        return find("slug", slug).firstResult();
    }

    
    public String getSlug() {
        return slug;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getPassword() {
		return password;
	}
    public LiveStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPassword(String password) {
		this.password = password;
	}
    public void setStatus(LiveStatus status) {
        this.status = status;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
