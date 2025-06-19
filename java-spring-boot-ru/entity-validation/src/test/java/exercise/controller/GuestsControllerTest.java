package exercise.controller;

import exercise.mapper.GuestMapper;
import exercise.model.Guest;
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

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.GuestRepository;

@SpringBootTest
@AutoConfigureMockMvc
class GuestsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestMapper mapper;

    private Guest testGuest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        testGuest = Instancio.of(Guest.class)
                .ignore(Select.field(Guest::getId))
                .supply(Select.field(Guest::getName), () -> faker.name().fullName())
                .supply(Select.field(Guest::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(Guest::getPhoneNumber), () -> "+" + faker.number().digits(12))
                .supply(Select.field(Guest::getClubCard), () -> faker.number().digits(4))
                .supply(Select.field(Guest::getCardValidUntil), () -> {
                    return faker.date().future(60, TimeUnit.DAYS).toLocalDateTime().toLocalDate();
                })
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
        guestRepository.save(testGuest);
        var result = mockMvc.perform(get("/guests"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        guestRepository.save(testGuest);

        var request = get("/guests/{id}", testGuest.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testGuest.getName()),
                v -> v.node("email").isEqualTo(testGuest.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testGuest);

        var request = post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var guest = guestRepository.findByPhoneNumber(testGuest.getPhoneNumber()).get();

        assertThat(guest).isNotNull();
        assertThat(guest.getName()).isEqualTo(testGuest.getName());
        assertThat(guest.getEmail()).isEqualTo(testGuest.getEmail());
        assertThat(guest.getPhoneNumber()).isEqualTo(testGuest.getPhoneNumber());
    }

    @Test
    public void testCreateWithNotValidEmail() throws Exception {
        var dto = mapper.map(testGuest);
        dto.setEmail("qwert");

        var request = post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithNotValidPhone() throws Exception {
        var dto = mapper.map(testGuest);
        dto.setPhoneNumber("+12345678912w");

        var request = post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithNotValidCard() throws Exception {
        var dto = mapper.map(testGuest);
        dto.setClubCard("12");

        var request = post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithExpiredCard() throws Exception {
        var dto = mapper.map(testGuest);
        dto.setCardValidUntil(faker.date().past(10, TimeUnit.DAYS).toLocalDateTime().toLocalDate());

        var request = post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
