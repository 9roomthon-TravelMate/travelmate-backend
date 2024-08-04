package travelmate.backend.dto;


public record TourSpotQueryRequest(
    Long regionId,
    Long districtId,
    Long themeId,
    Integer pageNumber,
    Integer pageSize
) {}
