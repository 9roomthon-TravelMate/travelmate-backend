package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.PostDto;
import travelmate.backend.entity.Post;
import travelmate.backend.entity.PostImage;
import travelmate.backend.repository.PostRepository;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImgStorageService imgStorageService;

    public Post create(PostDto postDto, MultipartFile image) throws IOException {
        List<PostImage> images = new ArrayList<>();
        if (image !=null && !image.isEmpty()) {
            String saveName = imgStorageService.storeFile(image);
            String filePath = "img_dir/" + saveName;
            PostImage img = PostImage.builder()
                    .originalImageName(image.getOriginalFilename())
                    .saveImageName(saveName)
                    .filePath(filePath)
                    .build();
            images.add(img);
        }

        Post post = Post.builder()
                        .title(postDto.getTitle())
                        .content(postDto.getContent())
                        .hashtags(postDto.getHashtags())
                        .images(images)
                        .build();

        // 이미지의 post 참조를 새로 생성된 post로 설정
        images.forEach(img -> img.setPost(post));
        Post savepost = postRepository.save(post);
        return savepost;

        }
    }
