package travelmate.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import travelmate.backend.dto.PostDto;
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

}
