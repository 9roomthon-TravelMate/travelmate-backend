package travelmate.backend.dto;

public record TourSpotReviewQueryRequest(
        Integer rating,
        Integer pageNumber,
        Integer pageSize
) {}
