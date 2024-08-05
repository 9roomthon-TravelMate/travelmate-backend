package travelmate.backend.dto;

import travelmate.backend.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record TourSpotDetailDto(
        Long id,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        Long regionId,
        Long districtId,
        Long themeId,
        String mainImageUrl,
        String mainThumbnailUrl,
        String overview,
        List<TourSpotImageDto> images
) {
    public TourSpotDetailDto(TourSpot tourSpot, TourSpotDetail tourSpotDetail, List<TourSpotImageDto> images) {
        this(
                tourSpot.getId(),
                tourSpot.getName(),
                tourSpot.getAddress(),
                tourSpot.getLatitude(),
                tourSpot.getLongitude(),
                Optional.ofNullable(tourSpot.getRegion())
                        .map(TourRegion::getId).orElse(null),
                Optional.ofNullable(tourSpot.getDistrict())
                        .map(TourDistrict::getId).orElse(null),
                Optional.ofNullable(tourSpot.getThemeDetail())
                        .map(TourSpotThemeDetail::getId).orElse(null),
                tourSpot.getMainImageUrl(),
                tourSpot.getMainThumbnailUrl(),
                tourSpotDetail.getOverview(),
                images
        );
    }
}
