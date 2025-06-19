package exercise.dto;

import lombok.Getter;
import lombok.Setter;

// BEGIN
@Setter
@Getter
public class ProductUpdateDTO {
    private String title;
    private int price;
}
// END
