package exercise.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class GuestDTO {
    private long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String clubCard;

    private LocalDate cardValidUntil;

    private LocalDate createdAt;
}
