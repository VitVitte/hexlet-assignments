package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDTO {
    @NotNull
    private Long assigneeId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
