package exercise.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Setter
@Getter
public class CarUpdateDTO {
    private JsonNullable<String> model;

    private JsonNullable<String> manufacturer;

    private JsonNullable<Integer> enginePower;
}
// END
