package com.zerhmouti.redditclone.mapper;

import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.dto.PostResponse;
import com.zerhmouti.redditclone.enumeration.VoteType;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.Subreddit;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.model.Vote;
import com.zerhmouti.redditclone.repo.CommentRepo;
import com.zerhmouti.redditclone.repo.VoteRepository;
import com.zerhmouti.redditclone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private AuthService authService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CommentRepo commentRepo;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post map(PostRequest postRequest, User user, Subreddit subreddit);

    @Mapping(target = "downVote", expression="java(getDownVote(post))")
    @Mapping(target="upVote", expression = "java(getUpVote(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "subredditName", source="subreddit.name")
    @Mapping(target="commentCount", expression = "java(commentCount(post))")
    public abstract PostResponse mapToDto(Post post);

    public String commentCount(Post post){
        return commentRepo.findAllByPost(post).size()+"";
    }

    public String getDuration(Post post){
        return Duration.between(post.getCreatedDate(), Instant.now()).toString();
    }

    public boolean getUpVote(Post post) {
        return checkVoteType(post, VoteType.UP);
    }
    public boolean getDownVote(Post post) {
        return checkVoteType(post, VoteType.DOWN);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if(post.getVoteCount()<=0 || !authService.isLoggedIn()) return false;
        Optional<Vote> voteForPostByUser =
                voteRepository.findTopByPostAndUser(post,
                        authService.getCurrentUser());
        return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                .isPresent();
    }
}
