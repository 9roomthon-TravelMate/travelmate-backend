package travelmate.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import travelmate.backend.entity.TourSpot;

import java.util.List;

public interface TourSpotRepository extends JpaRepository<TourSpot, Long> {

    @Query("SELECT ts FROM TourSpot ts " +
            "WHERE (:regionId IS NULL OR ts.region.id = :regionId)" +
            "AND (:districtId IS NULL OR ts.district.id = :districtId)" +
            "AND ((:themeId is null AND ts.themeDetail.id is not null) OR ts.themeDetail.id = :themeId)")
    Page<TourSpot> findByFilter(Long regionId, Long districtId, Long themeId, Pageable pageable);

    List<TourSpot> findAllByContentIdIn(Iterable<String> contentIds);
}
