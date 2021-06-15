package demo.hao.controller;

import com.netflix.discovery.EurekaClient;
import demo.hao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/client-reactive")
public class ClientReactiveController {

    @Autowired
    private WebClient.Builder clientBuilder;

    @Autowired
    private EurekaClient eurekaClient;

    @GetMapping
    public String ping() {
        return "hi";
    }

    @GetMapping("/posts")
    public Flux<Post> getAllPosts() {
        return clientBuilder.build().get().uri("http://post-service/posts")
                .retrieve()
                .bodyToFlux(Post.class);
    }
}
