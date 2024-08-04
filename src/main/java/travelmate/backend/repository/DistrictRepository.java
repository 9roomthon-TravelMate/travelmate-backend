package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travelmate.backend.entity.TourDistrict;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<TourDistrict, Long> {
    List<TourDistrict> findByRegionId(Long regionId);

    Optional<TourDistrict> findByRegionIdAndCode(Long regionId, String code);
}
