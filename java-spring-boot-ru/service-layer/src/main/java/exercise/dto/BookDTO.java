package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private Long authorId;
    private String authorFirstName;
    private String authorLastName;
    private String title;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
