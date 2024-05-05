package live.manager.entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Live extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "live_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
      name = "live_id_seq", 
      sequenceName = "live_id_seq"
    )
    public Long id;
    public String slug;
    public String title;
    public String description;
    public String password;
    @Enumerated(EnumType.STRING)
    public LiveStatus status;
    @Column(name = "created_at")
    public Timestamp createdAt;

    public Live() {}

    public Live(String slug, String title, String description, String password, LiveStatus status) {
		super();
		this.slug = slug;
		this.title = title;
		this.description = description;
		this.password = password;
		this.status = status;
		this.createdAt = Timestamp.from(Instant.now());
	}

	// Active record pattern 
    public static Uni<List<Live>> getAllLives() {
    	return findAll(Sort.by("title")).list();
		
	}
    public static Uni<Live> findBySlug(String slug){
        return find("slug", slug).firstResult();
    }

    
    public Long getId() {
        return id;
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
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
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
