package exercise.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class CategoryDTO {
    private long id;
    private String name;
    private LocalDate createdAt;
}
