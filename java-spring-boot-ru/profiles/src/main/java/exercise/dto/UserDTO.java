package exercise.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class UserDTO {
    private long id;

    private String name;

    private String email;

    private LocalDate createdAt;
}
