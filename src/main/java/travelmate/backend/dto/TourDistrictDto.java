package travelmate.backend.dto;

import travelmate.backend.entity.TourDistrict;
import travelmate.backend.entity.TourRegion;

public record TourDistrictDto(
    Long regionId,
    Long districtId,
    String name
) {
    public TourDistrictDto(TourDistrict district, TourRegion region) {
        this(
                region.getId(),
                district.getId(),
                district.getName()
        );
    }
}
