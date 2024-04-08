package com.zerhmouti.redditclone.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long voteId;
}
