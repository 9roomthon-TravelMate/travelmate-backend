package travelmate.backend.dto.tourApi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public record TourApiResponseBody<T>(
        Long numOfRows,
        Long pageNo,
        Long totalCount,
        List<T> items
) {
    @JsonCreator
    public TourApiResponseBody(
            Long numOfRows,
            Long pageNo,
            Long totalCount,
            @JsonProperty("items") ItemsWrap<T> itemsWrap
    ) {
        this(numOfRows, pageNo, totalCount, itemsWrap.items());
    }

    public record ItemsWrap<T>(
            @JsonProperty("item") List<T> items
    ) {}

}
