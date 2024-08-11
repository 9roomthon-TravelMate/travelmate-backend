package travelmate.backend.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record TourSpotReviewCreateRequest(
        @NotNull @Range(min = 1, max = 5) Integer rating,
        String text
) {}
