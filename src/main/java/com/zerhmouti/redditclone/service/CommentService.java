package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.dto.CommentDto;
import com.zerhmouti.redditclone.exception.PostNotFoundException;
import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.mapper.CommentMapper;
import com.zerhmouti.redditclone.model.Comment;
import com.zerhmouti.redditclone.model.NotificationEmail;
import com.zerhmouti.redditclone.model.Post;
import com.zerhmouti.redditclone.model.User;
import com.zerhmouti.redditclone.repo.CommentRepo;
import com.zerhmouti.redditclone.repo.PostRepository;
import com.zerhmouti.redditclone.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepo commentRepo;
    private final EmailService emailService;
    private final UserRepository userRepository;
    public Long save(CommentDto commentDto){
        User user = authService.getCurrentUser();
        Post post= postRepository.findById(commentDto.getPostId())
                       .orElseThrow(()-> new PostNotFoundException("Post doesn't found with id"+commentDto.getPostId()));
        Comment comment =commentMapper.map(commentDto,user,post);
        Comment savedComment = commentRepo.save(comment);

        emailService.send(
                NotificationEmail.builder()
                        .body(user.getUsername()+" posted a comment on your post")
                        .subject("Commented on your post")
                        .recipient(user.getEmail())
                        .build()
        );
        return savedComment.getId();
        }

        public List<CommentDto> getAllCommentsForPost(Long id){
            Post post = postRepository.findById(id)
                            .orElseThrow(()-> new PostNotFoundException(id.toString()));
            return commentRepo.findAllByPost(post)
                    .stream().map(commentMapper::maptoDto).collect(Collectors.toList());
        }

        public List<CommentDto> getAllCommentForUser(String userName){
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(()->new UsernameNotFoundException(userName));
            return commentRepo.findAllByUser(user)
                    .stream()
                    .map(commentMapper::maptoDto)
                    .toList();
        }
    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new RedditCloneException("Comments contains unacceptable language");
        }
        return false;
    }
}
