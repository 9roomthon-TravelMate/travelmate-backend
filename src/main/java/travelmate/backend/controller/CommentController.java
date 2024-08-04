package travelmate.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import travelmate.backend.dto.CommentDto;
import travelmate.backend.entity.Comment;
import travelmate.backend.repository.CommentRepository;
import travelmate.backend.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Comment> create(@RequestBody CommentDto commentDto) {
        Comment createcomment = commentService.createcomment(commentDto);
        return ResponseEntity.ok(createcomment);
    }
}
