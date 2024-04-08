package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.NotificationEmail;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.VerificationToken;
import com.zerhmouti.redditclone.repo.UserRepository;
import com.zerhmouti.redditclone.repo.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void register(PostRequest postRequest) throws RedditCloneException {
        User user = User.builder()
                .userName(postRequest.getUserName())
                .email(postRequest.getEmail())
                .password(passwordEncoder.encode(postRequest.getPassword()))
                .created(Instant.now())
                .enabled(false)
                .build();
        userRepository.save(user);
        String token = generateVerificationToken(user);
        emailService.send(NotificationEmail.builder()
                        .recipient(postRequest.getEmail())
                        .subject("Please activate your account")
                        .body("Thank you for signing up to Spring Reddit, " +
                               "please click on the below url to activate your account : "
                              +"http://localhost:8080/api/auth/accountVerification/"+token)
                .build());
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
    private void fetchUserAndEnable(VerificationToken verificationToken) throws RedditCloneException {
        Optional<User> user = userRepository.findById(verificationToken.getUser().getUserId());
        User user1 =user.orElseThrow(()-> new RedditCloneException("user not found with name +"+verificationToken.getUser().getUserName()));
        user1.setEnabled(true);
        userRepository.save(user1);
    }
}
