package travelmate.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetPostListDto {
    private Long postId;
    private String title;
    private String content;
    private String author;
    private List<GetPostListDto.CommentDto> comments;
    private Long likeCount;
    private Long commentCount;
    private List<String> hashtags;
    private List<GetPostListDto.ImageDto> images;

    @Data
    public static class CommentDto {
        private Long commentId;
        private String content;
        private String writer;
    }

    @Data
    public static class ImageDto {
        private Long id;
        private String originalImageName;
        private String saveImageName;
        private String filePath;
    }
}
