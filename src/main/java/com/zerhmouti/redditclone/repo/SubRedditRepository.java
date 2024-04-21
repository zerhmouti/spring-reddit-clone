package com.zerhmouti.redditclone.repo;

import com.zerhmouti.redditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubRedditRepository extends JpaRepository<Subreddit,Long> {
    public Optional<Subreddit> findByName(String name);
}
