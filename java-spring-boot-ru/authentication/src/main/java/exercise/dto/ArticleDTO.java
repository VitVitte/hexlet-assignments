package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ArticleDTO {
    private Long id;
    private String slug;
    private String author;
    private String title;
    private String content;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
