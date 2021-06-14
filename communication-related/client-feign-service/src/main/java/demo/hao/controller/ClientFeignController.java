package demo.hao.controller;

import demo.hao.PostProxy;
import demo.hao.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/client-feign")
public class ClientFeignController {

    @Autowired
    private PostProxy postProxy;

    @GetMapping
    public String ping() {
        return "hi";
    }

    @GetMapping("/posts")
    public Collection<Post> getAllPosts() {
        return postProxy.getAllPosts();
    }
}
