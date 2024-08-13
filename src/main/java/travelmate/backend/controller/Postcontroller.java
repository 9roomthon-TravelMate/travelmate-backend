package travelmate.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.*;
import travelmate.backend.entity.Post;
import travelmate.backend.repository.PostRepository;
import travelmate.backend.service.PostService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/post")
public class Postcontroller {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Post> createpost(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam("postDto") String postDtoJson,
            @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PostDto postDto = objectMapper.readValue(postDtoJson, PostDto.class);

        Post createdPost = postService.create(user, postDto.getTitle(), postDto.getContent(), postDto.getHashtags(), files);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/update")
    public ResponseEntity<Post> updatepost(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam("postId") Long postid,
            @RequestPart("post") PostDto postDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> file) throws IOException {

        Post updatedpost = postService.update(user, postid, postDto, file);
        return ResponseEntity.ok(updatedpost);
    }

//    @GetMapping("/read")
//    public ResponseEntity<Post> readpost(@RequestParam("postid") PostReadDto postReadDto) {
//        Post getpost = postService.read(postReadDto);
//        return ResponseEntity.ok(getpost);
//    }

    @GetMapping("/readlists")
    public ResponseEntity<List<GetPostListDto>> readlist(@AuthenticationPrincipal CustomOAuth2User user)
     {
        List<GetPostListDto> readlist = postService.readlist(user);
        return ResponseEntity.ok(readlist);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletepost(@RequestParam("postid") Long postId) {
        postService.delete(postId);
        return ResponseEntity.ok("delete");
    }

    @GetMapping("/community")
    public ResponseEntity<List<CommunityDto>> community() {
        List<CommunityDto> posts = postService.getAllPostsWithDetails();
        return ResponseEntity.ok(posts);
    }


}
