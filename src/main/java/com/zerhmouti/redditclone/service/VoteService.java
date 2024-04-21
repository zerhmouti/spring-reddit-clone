package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.dto.VoteDto;
import com.zerhmouti.redditclone.enumeration.VoteType;
import com.zerhmouti.redditclone.exception.PostNotFoundException;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.Vote;
import com.zerhmouti.redditclone.repo.PostRepository;
import com.zerhmouti.redditclone.repo.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public Long save(VoteDto voteDto){
        boolean s =false;
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException("Post not found with ID-"+voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUser(post,authService.getCurrentUser());
        if(voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new RedditCloneException("You have already voted by "+voteDto.getVoteType()+" for this post");
        }else if(voteByPostAndUser.isPresent() &&
                         !voteDto.getVoteType().name().equals(voteByPostAndUser.get().getVoteType().name())
        ){
            s = true;
        }
        if(VoteType.UP.equals(voteDto.getVoteType())){
            voteByPostAndUser.get().setVoteType(VoteType.UP);
            post.setVoteCount(post.getVoteCount()+1);
        }else{
            voteByPostAndUser.get().setVoteType(VoteType.DOWN);
            post.setVoteCount(post.getVoteCount()-1);
        }
        Vote savedVote;
        if(s){
            savedVote = voteRepository.save(voteByPostAndUser.get());
        }else{
            savedVote =voteRepository.save(mapToVote(voteDto, post));
        }
        postRepository.save(post);
        return savedVote.getVoteId();
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
