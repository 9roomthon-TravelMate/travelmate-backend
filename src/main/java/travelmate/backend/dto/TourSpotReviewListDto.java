package travelmate.backend.dto;

import org.springframework.data.domain.Page;
import travelmate.backend.projection.TourSpotReviewAggregation;

public record TourSpotReviewListDto(
        Page<TourSpotReviewDto> page,
        Long totalReviewCount,
        Long totalRatingSum
) {
    public TourSpotReviewListDto(Page<TourSpotReviewDto> page, TourSpotReviewAggregation aggregation) {
        this(
                page,
                aggregation.getReviewCount(),
                aggregation.getRatingSum()
        );
    }
}
