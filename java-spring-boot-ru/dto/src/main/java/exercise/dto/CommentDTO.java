package exercise.dto;

import lombok.*;

// BEGIN
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentDTO {
    private long id;
    private String body;
}
// END
