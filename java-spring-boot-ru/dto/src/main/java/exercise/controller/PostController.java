package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost
                .map(post -> ResponseEntity.ok(convertToPostDTO(post)))
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

        private PostDTO convertToPostDTO(Post post) {
        List<CommentDTO> comments = commentRepository.findByPostId(post.getId()).stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getBody()))
                .collect(Collectors.toList());

        return new PostDTO(post.getId(), post.getTitle(), post.getBody(), comments);
    }
}
// END
