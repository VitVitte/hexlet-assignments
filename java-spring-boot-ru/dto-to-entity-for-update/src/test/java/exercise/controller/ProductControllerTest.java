package exercise.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.ProductRepository;
import exercise.model.Product;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        testProduct = Instancio.of(Product.class)
                .ignore(Select.field(Product::getId))
                .supply(Select.field(Product::getTitle), () -> faker.book().title())
                .supply(Select.field(Product::getPrice), () -> faker.number().numberBetween(1, 100))
                .supply(Select.field(Product::getVendorCode), () -> faker.barcode().ean13())
                .create();
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        productRepository.save(testProduct);
        var result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        productRepository.save(testProduct);

        var request = get("/products/{id}", testProduct.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
            v -> v.node("title").isEqualTo(testProduct.getTitle()),
            v -> v.node("price").isEqualTo(testProduct.getPrice()),
            v -> v.node("vendorCode").isEqualTo(testProduct.getVendorCode())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new ProductCreateDTO();
        dto.setTitle(testProduct.getTitle());
        dto.setPrice(testProduct.getPrice());
        dto.setVendorCode(testProduct.getVendorCode());

        var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var product = productRepository.findByVendorCode(testProduct.getVendorCode()).get();

        assertThat(product).isNotNull();
        assertThat(product.getTitle()).isEqualTo(testProduct.getTitle());
        assertThat(product.getPrice()).isEqualTo(testProduct.getPrice());
    }

    @Test
    public void testUpdate() throws Exception {

        productRepository.save(testProduct);

        var data = new ProductUpdateDTO();
        data.setTitle("new title");
        data.setPrice(20);

        var request = put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var product = productRepository.findById(testProduct.getId()).get();

        assertThat(product.getTitle()).isEqualTo(data.getTitle());
        assertThat(product.getPrice()).isEqualTo(data.getPrice());
    }

    @Test
    public void testUpdateWithVendor() throws Exception {

        productRepository.save(testProduct);

        var data = new HashMap();
        data.put("title", "new title");
        data.put("price", 20);
        data.put("vendorCode", 123L);


        var request = put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var product = productRepository.findById(testProduct.getId()).get();

        assertThat(product.getTitle()).isEqualTo(data.get("title"));
        assertThat(product.getPrice()).isEqualTo(data.get("price"));
        assertThat(product.getVendorCode()).isEqualTo(testProduct.getVendorCode());
    }
}
