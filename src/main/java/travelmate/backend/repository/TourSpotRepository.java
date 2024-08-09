package travelmate.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import travelmate.backend.dto.TourSpotSummaryDto;
import travelmate.backend.entity.TourSpot;

import java.util.List;

public interface TourSpotRepository extends JpaRepository<TourSpot, Long> {

    @Query("SELECT new travelmate.backend.dto.TourSpotSummaryDto( " +
            "ts.id, ts.name, ts.address, ts.region.id, ts.district.id, ts.themeDetail.id, " +
            "ts.mainImageUrl, ts.mainThumbnailUrl, count(tsr.id), coalesce(sum(tsr.rating) , 0) " +
            ") FROM TourSpot ts left join TourSpotReview tsr ON ts.id = tsr.tourSpot.id " +
            "WHERE (:regionId IS NULL OR ts.region.id = :regionId) " +
            "AND (:districtId IS NULL OR ts.district.id = :districtId) " +
            "AND ((:themeId is null AND ts.themeDetail.id is not null) OR ts.themeDetail.id = :themeId) " +
            "GROUP BY ts.id")
    Page<TourSpotSummaryDto> findByFilter(Long regionId, Long districtId, Long themeId, Pageable pageable);

    List<TourSpot> findAllByContentIdIn(Iterable<String> contentIds);
}
