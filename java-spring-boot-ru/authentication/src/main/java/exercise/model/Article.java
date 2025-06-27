package exercise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "assignees")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Article implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Column(unique = true)
    private String slug;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
}
