package com.zerhmouti.redditclone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Subreddit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
}
