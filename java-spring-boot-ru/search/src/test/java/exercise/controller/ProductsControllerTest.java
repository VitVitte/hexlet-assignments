
package exercise.controller;

import exercise.mapper.ProductMapper;
import exercise.model.Product;
import exercise.model.Category;
import exercise.repository.ProductRepository;
import exercise.util.ModelGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.repository.CategoryRepository;

import java.math.BigDecimal;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Product testProduct;

    private Category anotherCategory;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        var category = Instancio.of(modelGenerator.getCategoryModel()).create();
        anotherCategory = Instancio.of(modelGenerator.getCategoryModel()).create();
        categoryRepository.save(category);
        categoryRepository.save(anotherCategory);
        testProduct = Instancio.of(modelGenerator.getProductModel()).create();
        testProduct.setCategory(category);
    }

    @Test
    public void testIndex() throws Exception {
        productRepository.save(testProduct);
        var result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().hasSize(10);
    }

    @Test
    public void testIndexWithTitleContains() throws Exception {
        var result = mockMvc.perform(get("/products?titleCont=lamp"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("title").asString().containsIgnoringCase("lamp"))
        );
    }

    @Test
    public void testIndexWithPrice() throws Exception {
        var result = mockMvc.perform(get("/products?priceGt=20&priceLt=50"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("price").asNumber()
                                .isGreaterThan(BigDecimal.valueOf(20)).isLessThan(BigDecimal.valueOf(50)))
        );
    }

    @Test
    public void testIndexWithRating() throws Exception {
        var result = mockMvc.perform(get("/products?ratingGt=4"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("rating").asNumber()
                                .isGreaterThan(BigDecimal.valueOf(4)))
        );
    }

    @Test
    public void testIndexWithCategory() throws Exception {
        var result = mockMvc.perform(get("/products?categoryId=1"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("categoryId").isEqualTo(1))
        );
    }

    @Test
    public void testIndexComplexCondition() throws Exception {
        var request = get("/products?priceGt=10&priceLt=80&CategoryId=1&ratingGt=3&titleCont=b");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("price").asNumber()
                                .isGreaterThan(BigDecimal.valueOf(10)).isLessThan(BigDecimal.valueOf(80)))
                        .and(v -> v.node("categoryId").isEqualTo(1))
                        .and(v -> v.node("price").asNumber().isGreaterThan(BigDecimal.valueOf(3)))
                        .and(v -> v.node("title").asString().containsIgnoringCase("b"))
        );
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
                v -> v.node("categoryId").isEqualTo(testProduct.getCategory().getId()),
                v -> v.node("categoryName").isEqualTo(testProduct.getCategory().getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testProduct);

        var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var product = productRepository.findByTitle(testProduct.getTitle()).get();

        assertThat(product).isNotNull();
        assertThat(product.getTitle()).isEqualTo(testProduct.getTitle());
        assertThat(product.getPrice()).isEqualTo(testProduct.getPrice());
        assertThat(product.getCategory().getId()).isEqualTo(testProduct.getCategory().getId());
    }

    @Test
    public void testCreateWithWrongCategory() throws Exception {
        var dto = mapper.map(testProduct);
        dto.setCategoryId(12345L);

        var request = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        productRepository.save(testProduct);

        var dto = mapper.map(testProduct);

        dto.setTitle("new title");
        dto.setPrice(123);
        dto.setCategoryId(anotherCategory.getId());

        var request = put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = productRepository.findById(testProduct.getId()).get();

        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getPrice()).isEqualTo(dto.getPrice());
        assertThat(task.getCategory().getId()).isEqualTo(dto.getCategoryId());
    }

    @Test
    public void testPartialUpdate() throws Exception {
        productRepository.save(testProduct);

        var dto = new HashMap<String, Long>();
        dto.put("categoryId", anotherCategory.getId());

        var request = put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = productRepository.findById(testProduct.getId()).get();

        assertThat(task.getTitle()).isEqualTo(testProduct.getTitle());
        assertThat(task.getPrice()).isEqualTo(testProduct.getPrice());
        assertThat(task.getCategory().getId()).isEqualTo(dto.get("categoryId"));
    }

    @Test
    public void testDestroy() throws Exception {
        productRepository.save(testProduct);
        var request = delete("/products/{id}", testProduct.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(productRepository.existsById(testProduct.getId())).isEqualTo(false);
    }
}
