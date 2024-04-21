package com.zerhmouti.redditclone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerhmouti.redditclone.dto.AuthRequest;
import com.zerhmouti.redditclone.dto.AuthenticationResponse;
import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.dto.RegisterRequest;
import com.zerhmouti.redditclone.enumeration.Role;
import com.zerhmouti.redditclone.enumeration.TokenType;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.JwtToken;
import com.zerhmouti.redditclone.model.NotificationEmail;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.VerificationToken;
import com.zerhmouti.redditclone.repo.JwtTokenRepository;
import com.zerhmouti.redditclone.repo.UserRepository;
import com.zerhmouti.redditclone.repo.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final JwtTokenRepository jwtTokenRepository;
    private final AuthenticationManager authenticationManager;
    @Transactional
    public void register(RegisterRequest postRequest) throws RedditCloneException {
        var user = User.builder()
                .userName(postRequest.getUsername())
                .email(postRequest.getEmail())
                .password(passwordEncoder.encode(postRequest.getPassword()))
                .created(Instant.now())
                .role(postRequest.getRole().equals("ADMIN")? Role.ADMIN: Role.MANAGER)
                .enabled(false)
                .build();
        var savedUser = userRepository.save(user);
//        var jwtToken = jwtService.generateToken(user);
//        var refrechToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser,jwtToken);
        String token = generateVerificationToken(user);
        emailService.send(NotificationEmail.builder()
                        .recipient(postRequest.getEmail())
                        .subject("Please activate your account")
                        .body("Thank you for signing up to Spring Reddit, " +
                               "please click on the below url to activate your account : "
                              +"http://localhost:8080/api/auth/accountVerification/"+token)
                .build());
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token= JwtToken.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) throws RedditCloneException {
        Optional<VerificationToken> token1 = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(token1.orElseThrow(() -> new RedditCloneException("Invalid Token")));
    }

    @Transactional
    protected void fetchUserAndEnable(VerificationToken verificationToken) throws RedditCloneException {
        Optional<User> user = userRepository.findById(verificationToken.getUser().getUserId());
        User user1 =user.orElseThrow(()-> new RedditCloneException("user not found with name +"+verificationToken.getUser().getUsername()));
        user1.setEnabled(true);
        userRepository.save(user1);
    }

    public AuthenticationResponse login(AuthRequest authRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        var user = userRepository.findByUserName(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found:"+authRequest.getUsername()));
        var jwtToken = jwtService.generateToken(user);
        var refrechToken = jwtService.generateRefreshToken(user);
        revokeAlluserTokens(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .refreshToken(refrechToken)
                .accessToken(jwtToken)
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByUserName(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAlluserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAlluserTokens(User user) {
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getUserId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(jwtToken -> {
            jwtToken.setRevoked(true);
            jwtToken.setExpired(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }


}
