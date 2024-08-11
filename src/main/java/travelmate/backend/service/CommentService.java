package travelmate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelmate.backend.dto.CommentDto;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.entity.Comment;
import travelmate.backend.entity.Member;
import travelmate.backend.entity.Post;
import travelmate.backend.repository.CommentRepository;
import travelmate.backend.repository.PostRepository;
import travelmate.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Comment createcomment(CustomOAuth2User user, CommentDto commentDto) {

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Member member = userRepository.findByUsername(user.getUsername());

        Comment createdcomment = Comment.builder()
                .content(commentDto.getContent())
                .writer(member.getNickname())
                .post(post)
                .build();

        commentRepository.save(createdcomment);
        return createdcomment;
    }
}
