package com.twitter.demo.services;


import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.repositories.CommentsRepository;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // Método corregido: ahora acepta userId como parámetro
    public void createComment(CreateCommentDto request, UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Comments comment = new Comments();
        comment.setAuthor(optionalUser.get());
        comment.setPost(optionalPost.get());
        comment.setMessage(request.getMessage());
        commentsRepository.save(comment);
    }

    // Método renombrado para coincidir con el controller
    public List<Comments> getCommentsByPost(UUID postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found");
        }
        return commentsRepository.findAllByPost(optionalPost.get());
    }

    // Método corregido: ahora acepta userId para validar permisos
    public void deleteComment(UUID commentId, UUID userId) {
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }

        Comments comment = optionalComment.get();

        // Validar que el usuario actual es el autor del comentario
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        commentsRepository.delete(comment);
    }
}