package travelmate.backend.dto;

import lombok.Data;

@Data
public class LikeDto {
    private Long postId;
    //private Long userId;  // 좋아요를 누른 사용자 ID
}
