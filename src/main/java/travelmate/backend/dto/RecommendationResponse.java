package travelmate.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponse {
    private Long travelerId;
    private List<String> recommendations;
}
