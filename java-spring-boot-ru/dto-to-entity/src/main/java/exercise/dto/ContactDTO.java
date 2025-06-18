package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ContactDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
