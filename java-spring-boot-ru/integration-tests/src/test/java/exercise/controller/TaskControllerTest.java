package exercise.controller;

import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


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
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShowTask() throws Exception {
        // Создаем задачу для теста
        Task task = createSampleTask();
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThatJson(json).node("id").isEqualTo(savedTask.getId());
                    assertThatJson(json).node("title").isEqualTo(savedTask.getTitle());
                    assertThatJson(json).node("description").isEqualTo(savedTask.getDescription());
                });
    }

    @Test
    public void testCreateTask() throws Exception {
        Task task = generateTask();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThatJson(json).node("title").isEqualTo(task.getTitle());
                    assertThatJson(json).node("description").isEqualTo(task.getDescription());
                });
    }
    @Test
    public void testUpdateTask() throws Exception {
        Task task = createSampleTask();
        task = taskRepository.save(task);

        Task updatedData = generateTask();

        mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThatJson(json).node("title").isEqualTo(updatedData.getTitle());
                    assertThatJson(json).node("description").isEqualTo(updatedData.getDescription());
                });
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task = createSampleTask();
        task = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());

        boolean exists = taskRepository.existsById(task.getId());
        assertThat(exists).isFalse();
    }

    private Task generateTask() {
        String title = faker.lorem().sentence(3);
        String description = faker.lorem().sentence(6);
        return new Task(title, description);
    }

    private Task createSampleTask() {
        String title = faker.lorem().sentence(3);
        String description = faker.lorem().sentence(6);
        return new Task(title, description);
    }
    // END
}
