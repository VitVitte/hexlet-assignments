package exercise.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exercise.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Transactional
    long deleteByPostId(long postId);

    List<Comment> findByPostId(long postId);
}
