package travelmate.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.PostDeleteDto;
import travelmate.backend.dto.PostDto;
import travelmate.backend.dto.PostReadDto;
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
            @RequestPart("post") PostDto postDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> file) throws IOException {

        Post createdpost = postService.create(postDto, file);
        return ResponseEntity.ok(createdpost);
    }

    @PutMapping("/update")
    public ResponseEntity<Post> updatepost(
            @RequestParam("postId") Long postid,
            @RequestPart("post") PostDto postDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> file) throws IOException {

        Post updatedpost = postService.update(postid, postDto, file);
        return ResponseEntity.ok(updatedpost);
    }

    @GetMapping("/read")
    public ResponseEntity<Post> readpost(@RequestParam("postid") PostReadDto postReadDto) {
        Post getpost = postService.read(postReadDto);
        return ResponseEntity.ok(getpost);
    }

    //user랑 연결되면 user id 값으로 받기
    @GetMapping("/readlists")
    public ResponseEntity<List<Post>> readlist() {
        List<Post> readlist = postService.readlist();
        return ResponseEntity.ok(readlist);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteepost(@RequestParam("postid")PostDeleteDto postDeleteDto) {
        postService.delete(postDeleteDto);
        return ResponseEntity.ok("delete");
    }

}
