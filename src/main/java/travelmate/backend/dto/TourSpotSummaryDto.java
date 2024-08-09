package travelmate.backend.dto;

import travelmate.backend.entity.TourDistrict;
import travelmate.backend.entity.TourRegion;
import travelmate.backend.entity.TourSpot;
import travelmate.backend.entity.TourSpotThemeDetail;
import travelmate.backend.projection.TourSpotReviewAggregation;

import java.util.Optional;

public record TourSpotSummaryDto(
        Long id,
        String name,
        String address,
        Long regionId,
        Long districtId,
        Long themeId,
        String mainImageUrl,
        String mainThumbnailUrl,
        Long reviewCount,
        Long ratingSum
) {
    public TourSpotSummaryDto(TourSpot tourSpot, TourSpotReviewAggregation aggregation) {
        this(
                tourSpot.getId(),
                tourSpot.getName(),
                tourSpot.getAddress(),
                Optional.ofNullable(tourSpot.getRegion())
                        .map(TourRegion::getId).orElse(null),
                Optional.ofNullable(tourSpot.getDistrict())
                        .map(TourDistrict::getId).orElse(null),
                Optional.ofNullable(tourSpot.getThemeDetail())
                        .map(TourSpotThemeDetail::getId).orElse(null),
                tourSpot.getMainImageUrl(),
                tourSpot.getMainThumbnailUrl(),
                aggregation.getReviewCount(),
                aggregation.getRatingSum()
        );
    }
}
