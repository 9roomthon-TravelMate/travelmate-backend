package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.TourSpotImage;

public interface TourSpotImageRepository extends JpaRepository<TourSpotImage, Long> {
}
