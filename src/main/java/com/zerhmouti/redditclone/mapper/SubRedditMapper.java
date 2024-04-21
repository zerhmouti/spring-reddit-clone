package com.zerhmouti.redditclone.mapper;

import com.zerhmouti.redditclone.dto.SubredditDto;
import com.zerhmouti.redditclone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(Subreddit subreddit){return subreddit.getPosts().size();  }

    @InheritInverseConfiguration
    @Mapping(target = "posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
