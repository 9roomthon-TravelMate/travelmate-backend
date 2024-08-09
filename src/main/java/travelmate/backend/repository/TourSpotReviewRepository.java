package travelmate.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import travelmate.backend.entity.TourSpotReview;
import travelmate.backend.projection.TourSpotReviewAggregation;

import java.util.Optional;

public interface TourSpotReviewRepository extends JpaRepository<TourSpotReview, Long> {

    Optional<TourSpotReview> findByTourSpotIdAndMemberId(Long tourSpotId, Long memberId);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT tr FROM TourSpotReview tr " +
            "WHERE (:rating IS NULL OR tr.rating = :rating) " +
            "AND (:tourSpotId IS NULL OR tr.tourSpot.id = :tourSpotId)")
    Page<TourSpotReview> findByFilter(Long tourSpotId, Integer rating, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Optional<TourSpotReview> findByIdAndMemberUsername(Long id, String username);

    @Query("SELECT count(*) AS reviewCount, coalesce(sum(tr.rating) , 0) AS ratingSum " +
            "FROM TourSpotReview tr " +
            "WHERE tr.tourSpot.id = :tourSpotId ")
    TourSpotReviewAggregation aggregateByTourSpotId(Long tourSpotId);

    @EntityGraph(attributePaths = {"member"})
    Optional<TourSpotReview> findByTourSpotIdAndMemberUsername(Long tourSpotId, String username);
}
