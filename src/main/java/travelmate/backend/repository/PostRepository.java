package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelmate.backend.dto.MyPagePostsDto;
import travelmate.backend.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query("SELECT new travelmate.backend.dto.MyPagePostsDto(p.id, pi.saveImageName, p.title, p.content, p.createdAt) " +
            "FROM Post p " +
            "JOIN p.images pi " +
            "WHERE p.user.id = :userId " +
            "AND pi.id = (SELECT MIN(pi2.id) FROM PostImage pi2 WHERE pi2.post.id = p.id) " +
            "ORDER BY p.createdAt DESC")
    List<MyPagePostsDto> findPostDetailsByUserId(@Param("userId") Long userId);

}
