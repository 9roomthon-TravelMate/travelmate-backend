package travelmate.backend.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import travelmate.backend.entity.PostImage;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MyPagePostsDto {

    private Long id;
    private String imageUri;
    private String title;
    private String content;
    private LocalDateTime createdAt;

}
