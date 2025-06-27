package exercise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class ArticleUpdateDTO {

    @NotBlank
    private JsonNullable<String> title;

    @NotBlank
    private JsonNullable<String> content;
}
