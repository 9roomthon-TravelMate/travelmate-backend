package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
