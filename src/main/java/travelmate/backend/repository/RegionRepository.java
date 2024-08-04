package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.TourRegion;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<TourRegion, Long> {
    Optional<TourRegion> findByCode(String code);
}
