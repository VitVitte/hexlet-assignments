package exercise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 3, max = 100)
    @NotNull
    private String passwordDigest;
}
