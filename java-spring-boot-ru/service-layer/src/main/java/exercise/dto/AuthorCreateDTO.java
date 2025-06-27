package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
