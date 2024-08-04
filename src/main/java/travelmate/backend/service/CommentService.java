package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelmate.backend.dto.CommentDto;
import travelmate.backend.entity.Comment;
import travelmate.backend.entity.Post;
import travelmate.backend.repository.CommentRepository;
import travelmate.backend.repository.PostRepository;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    public Comment createcomment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment createdcomment = Comment.builder()
                .content(commentDto.getContent())
                .writer(commentDto.getWriter())
                .post(post)
                .build();

        commentRepository.save(createdcomment);
        return createdcomment;
    }
}
