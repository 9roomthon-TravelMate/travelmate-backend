package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.TourSpotThemeDetail;

public interface TourSpotThemeRepository extends JpaRepository<TourSpotThemeDetail, Long> {

}
