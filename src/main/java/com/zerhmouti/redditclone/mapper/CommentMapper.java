package com.zerhmouti.redditclone.mapper;

import com.zerhmouti.redditclone.dto.CommentDto;
import com.zerhmouti.redditclone.model.Comment;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target="createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id",ignore = true)
    public Comment map(CommentDto commentDto, User user, Post post);

    @Mapping(target ="postId",expression="java(comment.getPost().getPostId())")
    @Mapping(target="userName", expression = "java(comment.getUser().getUsername())")
    public CommentDto maptoDto(Comment comment);
}
