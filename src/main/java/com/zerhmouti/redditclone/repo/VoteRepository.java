package com.zerhmouti.redditclone.repo;

import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    public Optional<Vote> findTopByPostAndUser(Post post, User user);
}
