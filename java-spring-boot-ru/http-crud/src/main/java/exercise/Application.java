package exercise;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    // Получить все посты
    @GetMapping("/posts")
    public List<Post> getPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "limit", defaultValue = "10") int limit) {
        int startIndex = (page - 1) * limit;
        int endIndex = Math.min(startIndex + limit, posts.size());

        if (startIndex >= posts.size()) {
            return Collections.emptyList();
        }

        return posts.subList(startIndex, endIndex);
    }

    // Получить пост по ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return ResponseEntity.ok(post);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Добавить новый пост
    @PostMapping("/posts")
    public Post addPost(@RequestBody Post newPost) {
        int newId = posts.size() > 0 ? posts.get(posts.size() - 1).getId() + 1 : 1;
        newPost.setId(newId);
        posts.add(newPost);
        return newPost;
    }

    // Обновить пост по ID
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == id) {
                updatedPost.setId(id);
                posts.set(i, updatedPost);
                return ResponseEntity.ok(updatedPost);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Удалить пост по ID
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post post = iterator.next();
            if (post.getId() == id) {
                iterator.remove();
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
    // END
}