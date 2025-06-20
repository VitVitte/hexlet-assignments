
package exercise.controller;

import exercise.mapper.TaskMapper;
import exercise.model.Task;
import exercise.model.User;
import exercise.repository.TaskRepository;
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
import exercise.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TasksControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(user);
        userRepository.save(anotherUser);
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(user);
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        taskRepository.save(testTask);

        var request = get("/tasks/{id}", testTask.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getTitle()),
                v -> v.node("description").isEqualTo(testTask.getDescription()),
                v -> v.node("assigneeId").isEqualTo(testTask.getAssignee().getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testTask);

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByTitle(testTask.getTitle()).get();

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
        assertThat(task.getDescription()).isEqualTo(testTask.getDescription());
        assertThat(task.getAssignee().getId()).isEqualTo(testTask.getAssignee().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);

        var dto = mapper.map(testTask);

        dto.setTitle("new title");
        dto.setDescription("new description");
        dto.setAssigneeId(anotherUser.getId());

        var request = put("/tasks/{id}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();

        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);
        var request = delete("/tasks/{id}", testTask.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }
}
