package com.zerhmouti.redditclone.controller;

import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.dto.PostResponse;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody PostRequest postRequest) throws RedditCloneException {
        return status(HttpStatus.CREATED).body("Post created with id"+postService.save(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return status(HttpStatus.OK)
                .body(postService.getAllPosts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) throws RedditCloneException {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    
}
