package com.twitter.demo.controllers;

import com.twitter.demo.entities.dto.CreatePostDto;
import com.twitter.demo.entities.dto.DislikeDto;
import com.twitter.demo.entities.dto.LikeDto;
import com.twitter.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostDto request){
        UUID userId = getCurrentUserId();
        postService.createPost(request, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes")
    public ResponseEntity<Void> likePost(@RequestBody LikeDto request){
        UUID userId = getCurrentUserId();
        postService.likePost(userId, request.getPostId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dislikes")
    public ResponseEntity<Void> dislikePost(@RequestBody DislikeDto request){
        UUID userId = getCurrentUserId();
        postService.dislikePost(userId, request.getPostId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Void> removeLike(@RequestBody LikeDto request){
        UUID userId = getCurrentUserId();
        postService.removeLike(userId, request.getPostId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dislikes")
    public ResponseEntity<Void> removeDislike(@RequestBody DislikeDto request){
        UUID userId = getCurrentUserId();
        postService.removeDislike(userId, request.getPostId());
        return ResponseEntity.noContent().build();
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authentication.getName());
    }
}
