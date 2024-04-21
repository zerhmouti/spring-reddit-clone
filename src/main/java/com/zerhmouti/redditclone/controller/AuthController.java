package com.zerhmouti.redditclone.controller;

import com.zerhmouti.redditclone.dto.AuthRequest;
import com.zerhmouti.redditclone.dto.AuthenticationResponse;
import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.dto.RegisterRequest;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.VerificationToken;
import com.zerhmouti.redditclone.repo.UserRepository;
import com.zerhmouti.redditclone.repo.VerificationTokenRepository;
import com.zerhmouti.redditclone.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody RegisterRequest postRequest) throws RedditCloneException {
        authService.register(postRequest);
        return ResponseEntity.ok("User Registration Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable("token") String token) throws RedditCloneException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated succefully",HttpStatus.OK);
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

}
