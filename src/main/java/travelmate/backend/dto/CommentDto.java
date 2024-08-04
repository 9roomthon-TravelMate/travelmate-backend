package travelmate.backend.dto;

import lombok.Data;
import travelmate.backend.entity.Post;

@Data
public class CommentDto {
    private String content;
    private String writer;
    private Long postId;
}
