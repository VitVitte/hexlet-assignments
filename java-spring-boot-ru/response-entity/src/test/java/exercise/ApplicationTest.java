package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.model.Post;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        Post testPost = new Post("test-post", "Test post", "Test body");
        Application.setPosts(new ArrayList<>(List.of(testPost)));
    }

    @Order(1)
    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/posts/test-post"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreatePost() throws Exception {
        var post = new Post("another-post", "another post", "body");

        var request = post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(om.writeValueAsString(post)));
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "1"));
    }

    @Test
    public void testShowUnknown() throws Exception {
        mockMvc.perform(get("/posts/unknown-post"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePost() throws Exception {
        var post = new Post("test-post", "new title", "new body");

        var request = put("/posts/test-post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(post)));

        mockMvc.perform(get("/posts/test-post"))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(post)));
    }

    @Test
    public void testUpdateUnknownPost() throws Exception {
        var post = new Post("unknown-post", "new title", "new body");

        var request = put("/posts/unknown-post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/posts/test-post"))
                .andExpect(status().isOk());

    }
}
