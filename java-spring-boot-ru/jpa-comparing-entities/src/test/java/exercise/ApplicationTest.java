package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

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
import exercise.repository.ProductRepository;
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

    private Product testProduct;

    @BeforeEach
    public void setUp() {
        testProduct = new Product();
        productRepository.save(testProduct);
    }

    @Test
    public void testWelcomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/products/{id}", testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testProduct)));
    }

    @Test
    public void testShowNegative() throws Exception {
        var result = mockMvc.perform(get("/products/{id}", 100))
                .andExpect(status().isNotFound())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Product with id 100 not found");
    }

    @Test
    public void testCreate() throws Exception {
        var product1 = new Product();
        product1.setTitle("orange");
        product1.setPrice(10);

        var request1 = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product1));

        mockMvc.perform(request1)
                .andExpect(status().isCreated());

        var product2 = new Product();
        product2.setTitle("orange");
        product2.setPrice(20);

        var request2 = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product2));

        mockMvc.perform(request2)
                .andExpect(status().isCreated());

        assertThat(productRepository.findAll()).hasSize(3);
    }

    @Test
    public void testNegative() throws Exception {
        var product = new Product();
        product.setTitle("orange");
        product.setPrice(10);

        var request1 = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product));

        mockMvc.perform(request1)
                .andExpect(status().isCreated());

        var request2 = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product));

        mockMvc.perform(request2)
                .andExpect(status().isConflict());

        assertThat(productRepository.findAll()).hasSize(2);
    }

    @Test
    public void testUpdate() throws Exception {
        var product = new Product();
        product.setTitle("orange");
        product.setPrice(10);

        var request = put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualProduct = productRepository.findById(testProduct.getId()).get();

        assertThat(actualProduct.getTitle()).isEqualTo(product.getTitle());
        assertThat(actualProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    public void testUpdateNegative() throws Exception {
        var product = new Product();
        product.setTitle("orange");
        product.setPrice(10);

        var request = put("/products/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(product));

        var result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Product with id 100 not found");
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/products/{id}", testProduct.getId()))
                .andExpect(status().isOk());

        assertThat(productRepository.findAll()).isEmpty();
    }
}
