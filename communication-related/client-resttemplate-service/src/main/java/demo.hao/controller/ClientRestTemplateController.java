package demo.hao.controller;

import demo.hao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/client")
public class ClientRestTemplateController {

    private static final String POST_SERVICE_URL = "http://post-service/posts/";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/posts")
    public Collection<Post> getAllPosts() {
        return restTemplate.getForObject(POST_SERVICE_URL, Collection.class);
    }
}
