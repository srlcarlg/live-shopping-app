package live.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestLiveStoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(LiveStoreApplication::main).with(TestLiveStoreApplication.class).run(args);
	}

}
