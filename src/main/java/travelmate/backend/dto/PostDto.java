package travelmate.backend.dto;

import lombok.Data;
import travelmate.backend.entity.PostImage;

import java.util.List;

@Data
public class PostDto {
    private String title;
    private String content;
    private List<String> hashtags;
}
