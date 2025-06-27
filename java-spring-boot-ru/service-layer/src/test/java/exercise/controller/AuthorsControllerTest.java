package exercise.controller;

import exercise.mapper.AuthorMapper;
import exercise.model.Author;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.repository.AuthorRepository;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorsControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Author testAuthor;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        testAuthor = Instancio.of(modelGenerator.getAuthorModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        authorRepository.save(testAuthor);
        var result = mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        authorRepository.save(testAuthor);

        var request = get("/authors/{id}", testAuthor.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testAuthor.getFirstName()),
                v -> v.node("lastName").isEqualTo(testAuthor.getLastName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testAuthor);

        var request = post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var author = authorRepository.findByFirstNameAndLastName(
                testAuthor.getFirstName(), testAuthor.getLastName()).get();

        assertThat(author).isNotNull();
        assertThat(author.getFirstName()).isEqualTo(testAuthor.getFirstName());
        assertThat(author.getLastName()).isEqualTo(testAuthor.getLastName());
    }

    @Test
    public void testCreateWithNotValidName() throws Exception {
        var dto = mapper.map(testAuthor);
        dto.setLastName("");

        var request = post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        authorRepository.save(testAuthor);

        var dto = mapper.map(testAuthor);

        dto.setFirstName("new name");
        dto.setLastName("new last name");

        var request = put("/authors/{id}", testAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var author = authorRepository.findById(testAuthor.getId()).get();

        assertThat(author.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(author.getLastName()).isEqualTo(dto.getLastName());
    }

    @Test
    public void testPartialUpdate() throws Exception {
        authorRepository.save(testAuthor);

        var dto = new HashMap<String, String>();
        dto.put("firstName", "another first name");

        var request = put("/authors/{id}", testAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var author = authorRepository.findById(testAuthor.getId()).get();

        assertThat(author.getLastName()).isEqualTo(testAuthor.getLastName());
        assertThat(author.getFirstName()).isEqualTo(dto.get("firstName"));
    }

    @Test
    public void testDestroy() throws Exception {
        authorRepository.save(testAuthor);
        var request = delete("/authors/{id}", testAuthor.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(authorRepository.existsById(testAuthor.getId())).isEqualTo(false);
    }
}
