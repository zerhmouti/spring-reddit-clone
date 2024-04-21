package com.zerhmouti.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long postId;
    private String PostName;
    private String url;
    private String description;
    private String subredditName;

    private Integer voteCount;
    private String commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;

}
