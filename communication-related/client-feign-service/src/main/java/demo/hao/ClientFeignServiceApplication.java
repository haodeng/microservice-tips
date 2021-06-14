package demo.hao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClientFeignServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientFeignServiceApplication.class, args);
    }
}
