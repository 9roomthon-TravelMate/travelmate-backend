package travelmate.backend.dto;

import travelmate.backend.entity.TourSpotImage;

public record TourSpotImageDto(
        String name,
        String url,
        String thumbnailUrl
) {
    public TourSpotImageDto(TourSpotImage tourSpotImage) {
        this(
                tourSpotImage.getName(),
                tourSpotImage.getUrl(),
                tourSpotImage.getThumbnailUrl()
        );
    }
}
