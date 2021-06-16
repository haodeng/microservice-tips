package demo.hao.controller;

import demo.hao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.function.Supplier;

@RestController
@RequestMapping("/client")
public class ClientRestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    @GetMapping
    public String ping() {
        return "hi, from " + instanceId;
    }

    @GetMapping("/posts")
    public Collection<Post> getAllPosts() {
        return restTemplate.getForObject("http://post-service/posts", Collection.class);
    }

    @GetMapping("/posts/slow")
    public Collection<Post> getAllPostsSlow() {
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("random-circuit");
        return circuitBreaker.run(() -> restTemplate.getForObject("http://post-service/posts/slow", Collection.class));
    }
}
