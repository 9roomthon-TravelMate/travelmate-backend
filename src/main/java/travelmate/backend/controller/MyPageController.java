package travelmate.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.dto.MyPagePostsDto;
import travelmate.backend.dto.TourSpotForRecommendDTO;
import travelmate.backend.repository.TourSpotRepository;
import travelmate.backend.repository.VisitedRepository;
import travelmate.backend.service.LikeService;
import travelmate.backend.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final LikeService likeService;
    private final PostService postService;
    private final VisitedRepository visitedRepository;
    private final TourSpotRepository tourSpotRepository;

    @GetMapping("/likes")
    public ResponseEntity<List<MyPagePostsDto>> getLikedPosts(@AuthenticationPrincipal CustomOAuth2User user) {
        List<MyPagePostsDto> likedPosts = likeService.getLikedPostsList(user.getId());
        return ResponseEntity.ok(likedPosts);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<MyPagePostsDto>> getPosts(@AuthenticationPrincipal CustomOAuth2User user) {
        List<MyPagePostsDto> likedPosts = postService.getMyPostsList(user.getId());
        return ResponseEntity.ok(likedPosts);
    }

    @GetMapping("/recommends")
    public ResponseEntity<List<TourSpotForRecommendDTO>> getRecommends(@AuthenticationPrincipal CustomOAuth2User user) {
        List<String> contentIds = visitedRepository.findContentIdsByTravelerId(user.getId());
        List<TourSpotForRecommendDTO> tourSpots = tourSpotRepository.findByContentIdIn(contentIds);

        return ResponseEntity.ok(tourSpots);
    }
}
