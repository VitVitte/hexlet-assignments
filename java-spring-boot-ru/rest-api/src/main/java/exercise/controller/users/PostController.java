package exercise.controller.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostController {
    private static final List<Post> posts = new ArrayList<>(Data.getPosts());

// Получить все посты пользователя по userId
    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> getPostsByUserId ( @PathVariable int id){
        List<Post> userPosts = posts.stream()
                .filter(post -> Objects.equals(post.getUserId(), id))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(userPosts.size()))
                .body(userPosts);
    }

    // Создать новый пост для пользователя по userId
    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> createPostForUser ( @PathVariable int id, @RequestBody Map<String, String> body){
        String slug = body.get("slug");
        String title = body.get("title");
        String bodyText = body.get("body");

        Post newPost = new Post();
        newPost.setUserId(id);
        newPost.setSlug(slug);
        newPost.setTitle(title);
        newPost.setBody(bodyText);

        posts.add(newPost);
        return ResponseEntity.status(201).body(newPost);
    }
}
// END

