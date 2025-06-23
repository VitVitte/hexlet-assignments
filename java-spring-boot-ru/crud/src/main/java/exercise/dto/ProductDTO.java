package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private Long categoryId;
    private  String categoryName;
    private String title;
    private int price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
