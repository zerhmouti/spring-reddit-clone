package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.RedditcloneApplication;
import com.zerhmouti.redditclone.dto.PostRequest;
import com.zerhmouti.redditclone.dto.PostResponse;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.mapper.PostMapper;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.Subreddit;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.repo.PostRepository;
import com.zerhmouti.redditclone.repo.SubRedditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostMapper postMapper;
    private final AuthService authService;
    private final SubRedditRepository subRedditRepository;
    private final PostRepository postRepository;

    public Long save(PostRequest postRequest) throws RedditCloneException {
        User user = authService.getCurrentUser();
        Subreddit subreddit = subRedditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()-> new RedditCloneException("Subreddit not found Exception with name :"+postRequest.getPostName()));
        Post post =postMapper.map(postRequest,user,subreddit);
        Post savedPost =postRepository.save(post);
        return savedPost.getPostId();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) throws RedditCloneException {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new RedditCloneException("Post not found exception with Id: "+id));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
