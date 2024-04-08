package com.zerhmouti.redditclone.repo;

import com.zerhmouti.redditclone.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    public Optional<VerificationToken> findByToken(String token);
}
