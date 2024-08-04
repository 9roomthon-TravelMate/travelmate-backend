package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.Like;
import travelmate.backend.entity.Post;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
//    boolean existsByPostAndUser(Post post, User user);
//    Optional<Like> findByPostAndUser(Post post, User user);

    boolean existsByPost(Post post);
    Optional<Like> findByPost(Post post);
    long countByPost(Post post);  // 게시글의 좋아요 수를 계산하는 메소드
}
