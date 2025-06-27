package exercise.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class AuthorDTO {
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;
}
