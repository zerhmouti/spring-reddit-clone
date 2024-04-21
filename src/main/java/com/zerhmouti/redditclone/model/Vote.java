package com.zerhmouti.redditclone.model;

import com.zerhmouti.redditclone.enumeration.VoteType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long voteId;
    @Enumerated(EnumType.STRING)
    private VoteType voteType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName ="userId")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postId", referencedColumnName ="postId")
    Post post;

}
