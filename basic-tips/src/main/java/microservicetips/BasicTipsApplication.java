package microservicetips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BasicTipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicTipsApplication.class, args);
	}

	@Bean
	HttpTraceRepository traceRepository() {
		return new InMemoryHttpTraceRepository();
	}
}
