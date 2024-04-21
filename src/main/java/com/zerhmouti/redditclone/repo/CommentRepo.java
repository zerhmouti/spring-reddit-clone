package com.zerhmouti.redditclone.repo;

import com.zerhmouti.redditclone.model.Comment;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByPost(Post post);

    Collection<Comment> findAllByUser(User user);
}
