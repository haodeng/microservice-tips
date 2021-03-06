package demo.hao.controller;

import demo.hao.model.Post;
import demo.hao.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public void addPost(@RequestBody Post post) {
        postRepository.add(post);
    }

    @GetMapping
    public Collection<Post> getAllPosts() {
        logger.info("get all posts");
        return postRepository.findAll();
    }

    @GetMapping("/slow")
    public Collection<Post> getAllPostsSlow() throws InterruptedException {
        logger.info("get all posts, slow");
        Thread.sleep(1000);
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(id);
    }

}
