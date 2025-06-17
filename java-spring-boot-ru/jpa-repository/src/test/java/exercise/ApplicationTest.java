package exercise;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import exercise.repository.ProductRepository;
import java.util.List;

import exercise.model.Product;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testWelcomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/products/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/products")
                    .param("min", "50")
                    .param("max", "80"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<Product> products = om.readValue(body, new TypeReference<List<Product>>() { });
        List<Integer> prices = products.stream().map(p -> p.getPrice()).toList();

        assertThat(prices).allMatch(p -> p >= 50 && p <= 80);
        assertThat(prices).isSorted();
    }

    @Test
    public void testIndexWithMinParam() throws Exception {

        var result = mockMvc.perform(get("/products")
                    .param("min", "50"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<Product> products = om.readValue(body, new TypeReference<List<Product>>() { });
        List<Integer> prices = products.stream().map(p -> p.getPrice()).toList();

        assertThat(prices).allMatch(p -> p >= 50);
        assertThat(prices).isSorted();
    }

    @Test
    public void testIndexWithMaxParam() throws Exception {

        var result = mockMvc.perform(get("/products")
                    .param("max", "40"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<Product> products = om.readValue(body, new TypeReference<List<Product>>() { });
        List<Integer> prices = products.stream().map(p -> p.getPrice()).toList();

        assertThat(prices).allMatch(p -> p <= 40);
        assertThat(prices).isSorted();
    }

    @Test
    public void testIndexWithoutParams() throws Exception {
        var result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<Product> products = om.readValue(body, new TypeReference<List<Product>>() { });

        assertThat(products).hasSize(20);
    }
}
