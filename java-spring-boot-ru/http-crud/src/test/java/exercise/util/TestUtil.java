package exercise.util;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;


import exercise.model.Post;

public class TestUtil {

    private static ObjectMapper om = new ObjectMapper();

    public static void createTestPost(MockMvc mockMvc, Post post) throws Exception {

        mockMvc.perform(
            post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post))
        )
            .andReturn()
            .getResponse();

    }
}
