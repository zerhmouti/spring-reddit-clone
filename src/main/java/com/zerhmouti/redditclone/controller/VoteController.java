package com.zerhmouti.redditclone.controller;

import com.zerhmouti.redditclone.dto.VoteDto;
import com.zerhmouti.redditclone.model.Vote;
import com.zerhmouti.redditclone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<String> vote(@RequestBody VoteDto voteDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Vote was created with ID-"+voteService.save(voteDto));
    }
}
