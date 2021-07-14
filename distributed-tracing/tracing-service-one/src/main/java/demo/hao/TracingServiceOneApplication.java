package demo.hao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TracingServiceOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracingServiceOneApplication.class, args);
    }
}
