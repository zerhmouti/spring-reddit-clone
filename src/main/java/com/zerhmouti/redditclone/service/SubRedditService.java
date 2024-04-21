package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.dto.SubredditDto;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.mapper.SubRedditMapper;
import com.zerhmouti.redditclone.model.Subreddit;
import com.zerhmouti.redditclone.repo.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;
    private final SubRedditMapper subRedditMapper;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit saved = subRedditRepository.save(subRedditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(saved.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){
        return subRedditRepository.findAll()
                .stream().map(this.subRedditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) throws RedditCloneException {
        var subreddit = subRedditRepository.findById(id)
                .orElseThrow(()-> new RedditCloneException("USER NOT FOUND WITH ID: "+id));
        return subRedditMapper.mapSubredditToDto(subreddit);
    }
//
//    private SubredditDto mapToDto(Subreddit subreddit){
//        return SubredditDto.builder()
//                .id(subreddit.getId())
//                .name(subreddit.getName())
//                .description(subreddit.getDescription())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }
//    private Subreddit mapSubRedditDto(SubredditDto subredditDto) {
//        return Subreddit.builder()
//                .description(subredditDto.getDescription())
//                .name(subredditDto.getName())
//                .build();
//    }
}
