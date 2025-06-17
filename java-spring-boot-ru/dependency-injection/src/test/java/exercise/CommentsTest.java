package exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.repository.CommentRepository;
import exercise.repository.PostRepository;

import exercise.model.Comment;
import exercise.model.Post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Post testPost1;
    private Post testPost2;
    private Comment testComment1;
    private Comment testComment2;

    @BeforeEach
    public void setUp() {
        testPost1 = new Post();
        testPost1.setTitle("post1 title");
        testPost1.setBody("post1 body");
        postRepository.save(testPost1);

        testComment1 = new Comment();
        testComment1.setPostId(testPost1.getId());
        testComment1.setBody("comment1 body");
        commentRepository.save(testComment1);

        testPost2 = new Post();
        testPost2.setTitle("post2 title");
        testPost2.setBody("post2 body");
        postRepository.save(testPost2);

        testComment2 = new Comment();
        testComment2.setPostId(testPost2.getId());
        testComment2.setBody("comment2 body");
        commentRepository.save(testComment2);
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/comments/{id}", testComment1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testComment1)));
    }

    @Test
    public void testShowNegative() throws Exception {
        var result = mockMvc.perform(get("/comments/{id}", 100))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testCreate() throws Exception {
        var comment = new Comment();
        comment.setBody("comment body ");

        var request = post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(comment));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        var comment = new Comment();
        comment.setBody("body");

        var request = put("/comments/{id}", testComment1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(comment));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualComment = commentRepository.findById(testComment1.getId()).get();

        assertThat(actualComment.getBody()).isEqualTo(comment.getBody());
    }

    @Test
    public void testUpdateNegative() throws Exception {
        var comment = new Comment();
        comment.setBody("body");

        var request = put("/products/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(comment));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/comments/{id}", testComment1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/posts/{id}", testPost2.getId()))
                .andExpect(status().isOk());

        assertThat(commentRepository.findAll()).isEmpty();
    }
}
