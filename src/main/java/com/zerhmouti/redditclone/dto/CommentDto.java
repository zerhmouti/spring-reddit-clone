package com.zerhmouti.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private String userName;
    private Long postId;
    private Instant createdDate;
}
