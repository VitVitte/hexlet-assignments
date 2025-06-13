package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import exercise.model.Post;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    public void testPosts() throws Exception {
        var post1 = new Post();
        post1.setSlug("another-post");
        post1.setTitle("another post");
        post1.setBody("body");

        var request1 = post("/api/users/999/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post1));

        post1.setUserId(999);

        mockMvc.perform(request1)
                .andExpect(status().isCreated())
                .andExpect(content().json(om.writeValueAsString(post1)));

        var post2 = new Post();
        post2.setSlug("another2-post");
        post2.setTitle("another2 post");
        post2.setBody("body2");

        var request2 = post("/api/users/999/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post2));

        post2.setUserId(999);

        mockMvc.perform(request2)
                .andExpect(status().isCreated())
                .andExpect(content().json(om.writeValueAsString(post2)));

        var posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        mockMvc.perform(get("/api/users/999/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(posts)));
    }
}
