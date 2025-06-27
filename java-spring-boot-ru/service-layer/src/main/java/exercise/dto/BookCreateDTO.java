package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookCreateDTO {
    @NotNull
    private Long authorId;

    @NotBlank
    private String title;
}
