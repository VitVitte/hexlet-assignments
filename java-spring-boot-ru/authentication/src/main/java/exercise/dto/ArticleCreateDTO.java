package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleCreateDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    @NotBlank
    private String content;
}
