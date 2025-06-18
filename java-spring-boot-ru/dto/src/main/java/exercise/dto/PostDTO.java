package exercise.dto;

import java.util.List;

import lombok.*;

// BEGIN
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private long id;
    private String title;
    private String body;
    private List<CommentDTO> comments;
}
// END
