package demo.hao;

import demo.hao.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(name = "post-service", path = "/posts")
public interface PostProxy {

    @GetMapping
    Collection<Post> getAllPosts();
}
