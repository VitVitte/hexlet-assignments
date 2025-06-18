package exercise.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactCreateDTO {
    private String firstName;
    private String lastName;
    private String phone;
}
