package travelmate.backend.dto.tourApi;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName(value = "response")
public record TourApiResponse<T> (
        TourApiResponseHeader header,
        TourApiResponseBody<T> body
) {}
