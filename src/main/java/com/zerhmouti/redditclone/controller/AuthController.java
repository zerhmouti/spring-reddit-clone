package com.zerhmouti.redditclone.controller;

import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.VerificationToken;
import com.zerhmouti.redditclone.repo.UserRepository;
import com.zerhmouti.redditclone.repo.VerificationTokenRepository;
import com.zerhmouti.redditclone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final VerificationTokenRepository tokenRepo;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody PostRequest postRequest) throws RedditCloneException {
        authService.register(postRequest);
        return new ResponseEntity<String>("Account is created", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable("token") String token) throws RedditCloneException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated succefully",HttpStatus.OK);
    }
}
