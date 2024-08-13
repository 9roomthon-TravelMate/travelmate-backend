package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.*;
import travelmate.backend.entity.Member;
import travelmate.backend.entity.Post;
import travelmate.backend.entity.PostImage;
import travelmate.backend.repository.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ImgStorageService imgStorageService;

    public Post create(CustomOAuth2User user, String title, String content, List<String> hashtags, List<MultipartFile> image) throws IOException {

        Member member = userRepository.findByUsername(user.getUsername());

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
                .title(title)
                .content(content)
                .hashtags(hashtags)
                .images(images)
                .user(member)
                .build();

        // 이미지의 post 참조를 새로 생성된 post로 설정
        images.forEach(img -> img.setPost(post));
        Post savepost = postRepository.save(post);
        return savepost;
    }

    public Post update(CustomOAuth2User user, Long postId, PostDto postDto, List<MultipartFile> newImages) throws IOException {
        Member updatemember = userRepository.findByUsername(user.getUsername());

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Post not found");
        }

        Post post = optionalPost.get();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setHashtags(postDto.getHashtags());
        post.setUser(updatemember);

        postRepository.save(post);
        return post;
    }

//    public Post read(PostReadDto postReadDto) {
//        Optional<Post> optionalPost = postRepository.findById(postReadDto.getId());
//        if (!optionalPost.isPresent()) {
//            throw new RuntimeException("Post not found");
//        }
//        Post findpost = optionalPost.get();
//        return findpost;
//    }

    public List<GetPostListDto> readlist(CustomOAuth2User user) {
        Member member = userRepository.findByUsername(user.getUsername());
        List<Post> getlistsbyuser = postRepository.findByUserId(member.getId());

        if (getlistsbyuser.isEmpty()) {
            // 비어 있는 경우 빈 리스트를 반환합니다.
            return new ArrayList<>();
        }

        return getlistsbyuser.stream().map(post -> {
            GetPostListDto dto = new GetPostListDto();
            dto.setPostId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setHashtags(post.getHashtags());
            dto.setAuthor(member.getNickname()); // getUsername() 대신 getNickname()으로 변경

            List<GetPostListDto.CommentDto> comments = commentRepository.findByPostId(post.getId())
                    .stream().map(comment -> {
                        GetPostListDto.CommentDto commentDto = new GetPostListDto.CommentDto();
                        commentDto.setCommentId(comment.getId());
                        commentDto.setContent(comment.getContent());
                        commentDto.setWriter(comment.getWriter());
                        return commentDto;
                    }).collect(Collectors.toList());

            List<GetPostListDto.ImageDto> images = post.getImages()
                    .stream().map(image -> {
                        GetPostListDto.ImageDto imageDto = new GetPostListDto.ImageDto();
                        imageDto.setId(image.getId());
                        imageDto.setOriginalImageName(image.getOriginalImageName());
                        imageDto.setSaveImageName(image.getSaveImageName());
                        imageDto.setFilePath(image.getFilePath());
                        return imageDto;
                    }).collect(Collectors.toList());

            dto.setComments(comments);
            dto.setImages(images);
            dto.setLikeCount(likeRepository.countByPost(post));
            dto.setCommentCount(commentRepository.countByPostId(post.getId()));

            return dto;
        }).collect(Collectors.toList());
    }


    public void delete(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            throw new RuntimeException("Post not found");
        }
        Post post = optionalPost.get();
        postRepository.delete(post);
    }

    public List<CommunityDto> getAllPostsWithDetails() {
        List<Post> posts = postRepository.findAll();

        // 포스트 리스트가 비어 있을 때 처리
        if (posts.isEmpty()) {
            // 비어 있는 경우 빈 리스트를 반환합니다.
            return new ArrayList<>();
        }

        return posts.stream().map(post -> {
            CommunityDto dto = new CommunityDto();
            dto.setPostId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setHashtags(post.getHashtags());
            dto.setAuthor(post.getUser().getNickname()); // Assuming getUsername() is the method to get the author's name

            List<CommunityDto.CommentDto> comments = commentRepository.findByPostId(post.getId())
                    .stream().map(comment -> {
                        CommunityDto.CommentDto commentDto = new CommunityDto.CommentDto();
                        commentDto.setCommentId(comment.getId());
                        commentDto.setContent(comment.getContent());
                        commentDto.setWriter(comment.getWriter());
                        return commentDto;
                    }).collect(Collectors.toList());

            List<CommunityDto.ImageDto> images = post.getImages()
                    .stream().map(image -> {
                        CommunityDto.ImageDto imageDto = new CommunityDto.ImageDto();
                        imageDto.setId(image.getId());
                        imageDto.setOriginalImageName(image.getOriginalImageName());
                        imageDto.setSaveImageName(image.getSaveImageName());
                        imageDto.setFilePath(image.getFilePath());
                        return imageDto;
                    }).collect(Collectors.toList());

            dto.setComments(comments);
            dto.setImages(images);
            dto.setLikeCount(likeRepository.countByPost(post));
            dto.setCommentCount(commentRepository.countByPostId(post.getId()));

            return dto;
        }).collect(Collectors.toList());
    }


    public List<MyPagePostsDto> getMyPostsList(Long userId){
        return postRepository.findPostDetailsByUserId(userId);
    }


}
