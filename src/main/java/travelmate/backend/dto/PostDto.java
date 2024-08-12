package travelmate.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PostDto {
    private String title;
    private String content;
    private List<String> hashtags;
}
