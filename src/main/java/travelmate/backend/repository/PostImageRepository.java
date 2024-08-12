package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
