package exercise.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCreateDTO {
    private String title;
    private int price;
    private long vendorCode;
}
