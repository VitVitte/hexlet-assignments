package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Long assigneeId;
    private String title;
    private String description;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
