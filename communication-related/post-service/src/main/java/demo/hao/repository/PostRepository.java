package demo.hao.repository;

import demo.hao.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PostRepository {
    private Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(Long id) {
        return posts.get(id);
    }

    public void add(Post post) {
        posts.putIfAbsent(post.getId(), post);
    }
}
