package demo.hao;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ClientReactiveServiceApplication {

    @Bean
    WebClient.Builder clientBuilder()  {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .doOnConnected(
                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS)));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder().clientConnector(connector);
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientReactiveServiceApplication.class, args);
    }
}
