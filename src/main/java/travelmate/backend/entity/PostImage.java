package travelmate.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")  // 테이블 이름 지정
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 엔티티의 식별자 필드

    @Column(nullable = false)
    private String originalImageName;  // 원본 이미지 파일명

    @Column(nullable = false)
    private String saveImageName;  // 저장된 이미지 파일명

    @Column(nullable = false)
    private String filePath;  // 이미지 파일 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")  // 외래 키
    @JsonBackReference
    private Post post;  // Post 엔티티와의 관계

}
