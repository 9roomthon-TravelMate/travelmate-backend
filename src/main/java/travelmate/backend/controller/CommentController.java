package travelmate.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import travelmate.backend.dto.CommentDto;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.entity.Comment;
import travelmate.backend.repository.CommentRepository;
import travelmate.backend.service.CommentService;

import java.io.IOException;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Comment> create(@AuthenticationPrincipal CustomOAuth2User user,
                                          @RequestParam("commentDto") String commentDtoJson) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CommentDto commentDto = objectMapper.readValue(commentDtoJson, CommentDto.class);

        Comment createdcomment = commentService.createcomment(user, commentDto);
        return ResponseEntity.ok(createdcomment);
    }
}
