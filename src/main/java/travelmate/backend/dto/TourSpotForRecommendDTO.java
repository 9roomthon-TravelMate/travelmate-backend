package travelmate.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TourSpotForRecommendDTO {
    private String name;
    private String address;
    private String mainThumbnailUrl;
}
