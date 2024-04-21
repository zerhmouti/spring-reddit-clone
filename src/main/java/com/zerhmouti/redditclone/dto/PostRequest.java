package com.zerhmouti.redditclone.dto;

import com.zerhmouti.redditclone.enumeration.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostRequest {
    private Long postId;
    private String postName;
    private String subredditName;
    @NotBlank(message = "Post Name connot be empty or null")
    private String url;
    private String description;
}
