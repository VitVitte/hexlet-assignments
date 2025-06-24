package exercise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductParamsDTO {
    private String titleCont;
    private Long categoryId;
    private Integer priceLt;
    private Integer priceGt;
    private Double ratingGt;
}
