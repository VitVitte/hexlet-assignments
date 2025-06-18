package exercise.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.ContactRepository;
import exercise.model.Contact;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactRepository contactRepository;

    private Contact testContact;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        testContact = Instancio.of(Contact.class)
                .ignore(Select.field(Contact::getId))
                .supply(Select.field(Contact::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(Contact::getLastName), () -> faker.name().lastName())
                .supply(Select.field(Contact::getPhone), () -> faker.phoneNumber().toString())
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
    public void testCreate() throws Exception {
        var dto = new ContactCreateDTO();
        dto.setFirstName(testContact.getFirstName());
        dto.setLastName(testContact.getLastName());
        dto.setPhone(testContact.getPhone());

        var request = post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        var createdContact = om.readValue(body, ContactDTO.class);

        var contact = contactRepository.findById(createdContact.getId()).get();

        assertThat(contact).isNotNull();
        assertThat(createdContact).usingRecursiveComparison().isEqualTo(contact);
    }
}
