package exercise.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.GeneratedValue;


@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private long postId;
    private String body;
}
