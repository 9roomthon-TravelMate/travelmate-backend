package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.PostDto;
import travelmate.backend.entity.Post;
import travelmate.backend.entity.PostImage;
import travelmate.backend.repository.PostImageRepository;
import travelmate.backend.repository.PostRepository;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private ImgStorageService imgStorageService;

    public Post create(PostDto postDto, List<MultipartFile> image) throws IOException {
        //이미지 리스트 저장
        List<PostImage> images = new ArrayList<>();
        //이미지가 있을때의 로직
        if (image != null) {
            for (MultipartFile file : image) {
                String saveName = imgStorageService.storeFile(file);
                String filePath = "img_dir/" + saveName;
                PostImage img = PostImage.builder()
                        .originalImageName(file.getOriginalFilename())
                        .saveImageName(saveName)
                        .filePath(filePath)
                        .build();
                images.add(img);
                postImageRepository.save(img);
            }
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

    public Post update(Long postId, PostDto postDto, List<MultipartFile> newImages) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Post not found");
        }

        Post post = optionalPost.get();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setHashtags(postDto.getHashtags());
//
//        // 기존 이미지 제거
//        post.getImages().clear();
//
//
//        // 새로운 이미지 저장
//        if (newImages != null) {
//            List<PostImage> updatedImages = new ArrayList<>();
//            for (MultipartFile file : newImages) {
//                if (!file.isEmpty() && !"".equals(file.getOriginalFilename())) {
//                    String saveName = imgStorageService.storeFile(file);
//                    String filePath = "img_dir/" + saveName;
//                    PostImage img = PostImage.builder()
//                            .originalImageName(file.getOriginalFilename())
//                            .saveImageName(saveName)
//                            .filePath(filePath)
//                            .build();
//                    img.setPost(post);
//                    updatedImages.add(img);
//                }
//            }
//            post.setImages(updatedImages);
//        }
        postRepository.save(post);
        return post;
    }

    public Post read(Long postid) {
        Optional<Post> optionalPost = postRepository.findById(postid);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Post not found");
        }
        Post findpost = optionalPost.get();
        return findpost;
    }
    public void delete(Long postid) {
        Optional<Post> optionalPost = postRepository.findById(postid);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Post not found");
        }
        Post post = optionalPost.get();
        postRepository.delete(post);
    }
}


