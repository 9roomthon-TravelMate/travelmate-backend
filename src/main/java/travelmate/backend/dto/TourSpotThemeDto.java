package travelmate.backend.dto;

import travelmate.backend.entity.TourSpotThemeDetail;

public record TourSpotThemeDto(
        Long id,
        String title
) {
    public TourSpotThemeDto(TourSpotThemeDetail themeDetail) {
        this(
                themeDetail.getId(),
                themeDetail.getTitle()
        );
    }
}
