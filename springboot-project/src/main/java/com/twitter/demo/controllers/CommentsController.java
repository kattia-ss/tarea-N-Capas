package com.twitter.demo.controllers;

import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.services.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CreateCommentDto request) {
        UUID userId = getCurrentUserId();
        commentsService.createComment(request, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comments>> getCommentsByPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(commentsService.getCommentsByPost(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        UUID userId = getCurrentUserId();
        commentsService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authentication.getName());
    }
}