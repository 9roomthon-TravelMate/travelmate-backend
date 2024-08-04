package travelmate.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import travelmate.backend.dto.LikeDto;
import travelmate.backend.service.LikeService;

@Controller
@RequestMapping("/like")
public class Likecontroller {

    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    public ResponseEntity<Void> addlike(@RequestBody LikeDto likeDto) {
        likeService.addLike(likeDto);
        //likeService.addLike(likeDto.getPostId(), likeDto.getUserId());
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @PostMapping("/remove")
    public ResponseEntity<Void> removeLike(@RequestBody LikeDto likeDto) {
        likeService.removeLike(likeDto);
        return ResponseEntity.ok().build();
    }

    // 게시글의 좋아요 수 확인
    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(@RequestParam("postId") Long postId) {
        long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }
}
