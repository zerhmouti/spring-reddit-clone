package com.zerhmouti.redditclone.repo;

import com.zerhmouti.redditclone.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Integer> {
    @Query(value = """
      select t from JwtToken t inner join User u\s
      on t.user.userId = u.userId\s
      where u.userId = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<JwtToken> findAllValidTokenByUser(Long id);

    Optional<JwtToken> findByToken(String token);
}
