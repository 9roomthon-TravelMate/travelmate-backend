package travelmate.backend.dto;

import travelmate.backend.entity.TourRegion;

public record TourRegionDto(
        Long id,
        String name,
        String englishName,
        String imageUrl
) {
    public TourRegionDto(TourRegion region) {
        this(
                region.getId(),
                region.getName(),
                region.getEnglishName(),
                region.getImageUrl()
        );
    }
}
