package demo.hao.controller;

import com.netflix.discovery.EurekaClient;
import demo.hao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/client")
public class ClientRestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EurekaClient eurekaClient;

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
}
