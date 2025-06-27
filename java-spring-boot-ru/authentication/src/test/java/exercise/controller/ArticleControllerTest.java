
package exercise.controller;

import exercise.dto.ArticleUpdateDTO;
import exercise.mapper.ArticleMapper;
import exercise.model.Article;
import exercise.repository.ArticleRepository;
import exercise.util.ModelGenerator;
import exercise.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private UserUtils userUtils;

    private Article testArticle;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testArticle = Instancio.of(modelGenerator.getArticleModel()).create();
        testArticle.setAuthor(userUtils.getTestUser());
        token = jwt().jwt(builder -> builder.subject(userUtils.getTestUser().getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        articleRepository.save(testArticle);
        var result = mockMvc.perform(get("/articles").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        articleRepository.save(testArticle);

        var request = get("/articles/{id}", testArticle.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testArticle.getTitle()),
                v -> v.node("content").isEqualTo(testArticle.getContent()),
                v -> v.node("author").isEqualTo(testArticle.getAuthor().getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testArticle);

        var request = post("/articles")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = articleRepository.findBySlug(testArticle.getSlug()).get();

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(testArticle.getTitle());
        assertThat(task.getContent()).isEqualTo(testArticle.getContent());
        assertThat(task.getAuthor().getId()).isEqualTo(testArticle.getAuthor().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        articleRepository.save(testArticle);

        var data = new ArticleUpdateDTO();
        data.setTitle(JsonNullable.of("new title"));


        var request = put("/articles/{id}", testArticle.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = articleRepository.findById(testArticle.getId()).get();

        assertThat(task.getTitle()).isEqualTo(data.getTitle().get());
        assertThat(task.getContent()).isEqualTo(testArticle.getContent());
    }

    @Test
    public void testDestroy() throws Exception {
        articleRepository.save(testArticle);
        var request = delete("/articles/{id}", testArticle.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(articleRepository.existsById(testArticle.getId())).isFalse();
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {

        var result = mockMvc.perform(get("/articles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testShowWithoutAuth() throws Exception {
        var request = get("/articles/{id}", testArticle.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateWithoutAuth() throws Exception {
        var dto = mapper.map(testArticle);

        var request = post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
}
