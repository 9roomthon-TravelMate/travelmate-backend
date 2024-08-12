package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelmate.backend.dto.LikeDto;
import travelmate.backend.dto.MyPagePostsDto;
import travelmate.backend.entity.Like;
import travelmate.backend.entity.Post;
import travelmate.backend.repository.LikeRepository;
import travelmate.backend.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

//    @Autowired
//    private UserRepository userRepository;

    // 좋아요 추가
    public void addLike(LikeDto likeDto) {
        Post post = postRepository.findById(likeDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 좋아요가 존재하는지 확인
//        boolean alreadyLiked = likeRepository.existsByPostAndUser(post, user);
        boolean alreadyLiked = likeRepository.existsByPost(post);

//        if (!alreadyLiked) {
//            Like like = Like.builder()
//                    .post(post)
//                    .user(user)
//                    .build();
//            likeRepository.save(like);
//        }
        if (!alreadyLiked) {
            Like like = Like.builder()
                    .post(post)
                    .build();
            likeRepository.save(like);
        }
    }

    // 좋아요 취소
    public void removeLike(LikeDto likeDto) {
        Post post = postRepository.findById(likeDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = likeRepository.findByPost(post)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
    }

    public Long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepository.countByPost(post);
    }

    public List<MyPagePostsDto> getLikedPostsList(Long userId) {
        List<Post> likedPosts = likeRepository.findLikedPostsByUserId(userId);
        return likedPosts.stream()
                .map(post -> new MyPagePostsDto(post.getId(), post.getImages().get(0).getSaveImageName()))
                .collect(Collectors.toList());
    }
}
