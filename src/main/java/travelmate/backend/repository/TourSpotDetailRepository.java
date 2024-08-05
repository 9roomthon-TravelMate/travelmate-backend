package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.TourSpotDetail;

public interface TourSpotDetailRepository extends JpaRepository<TourSpotDetail, Long> {
}
