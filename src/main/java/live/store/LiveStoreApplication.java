package live.store;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiveStoreApplication {
	
    private static final Logger log = LoggerFactory.getLogger(LiveStoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LiveStoreApplication.class, args);
	}
	
    @Bean
    public CommandLineRunner demo(ProductRepository repository) {

        return (args) -> {
            // save a few Products
            repository.saveAll(Arrays.asList(
        		new Product("randomSlug", "Product 1", 29.99, 10, 60, "https://example.com/image1.jpg", Timestamp.from(Instant.now())),
        		new Product("anotherSlug", "Product 2", 49.99, 5, 120, "https://example.com/image2.jpg", Timestamp.from(Instant.now())),
        		new Product("someSlug", "Product 3", 9.99, 20, 90, "https://example.com/image3.jpg", Timestamp.from(Instant.now()))
            )).blockLast(Duration.ofSeconds(10));

            log.info("Products found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(customer -> {
                log.info(customer.toString());
            }).blockLast(Duration.ofSeconds(10));
        };
    }
}
