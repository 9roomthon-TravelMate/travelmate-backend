package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
