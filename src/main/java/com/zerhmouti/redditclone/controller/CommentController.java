package com.zerhmouti.redditclone.controller;

import com.zerhmouti.redditclone.dto.CommentDto;
import com.zerhmouti.redditclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<String> create(@RequestBody CommentDto commentDto){
        Long savedCommentId = commentService.save(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Comment added successfully with id "+ savedCommentId);
    }

    @GetMapping(params = "postId")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@RequestParam Long postId){
        return ResponseEntity
                .ok(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping(params = "userName")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@RequestParam String userName){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentForUser(userName));
    }

}
