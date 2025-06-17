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
import exercise.repository.PostRepository;

import exercise.model.Post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostRepository postRepository;

    private Post testPost;

    @BeforeEach
    public void setUp() {
        testPost = new Post();
        testPost.setTitle("test title");
        testPost.setBody("test body");
        postRepository.save(testPost);
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/posts/{id}", testPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testPost)));
    }

    @Test
    public void testShowNegative() throws Exception {
        var result = mockMvc.perform(get("/posts/{id}", 100))
                .andExpect(status().isNotFound())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Post with id 100 not found");
    }

    @Test
    public void testCreate() throws Exception {
        var post = new Post();
        post.setTitle("post title");
        post.setBody("post body ");

        var request = post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        var post = new Post();
        post.setTitle("title");
        post.setBody("body");

        var request = put("/posts/{id}", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualPost = postRepository.findById(testPost.getId()).get();

        assertThat(actualPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(actualPost.getBody()).isEqualTo(post.getBody());
    }

    @Test
    public void testUpdateNegative() throws Exception {
        var post = new Post();
        post.setTitle("post");
        post.setBody("body");

        var request = put("/posts/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/posts/{id}", testPost.getId()))
                .andExpect(status().isOk());

        assertThat(postRepository.findAll()).isEmpty();
    }
}
