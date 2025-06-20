package exercise.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarCreateDTO {
    private String model;
    private String manufacturer;
    private int enginePower;
}
