package com.zerhmouti.redditclone.exception;

public class SubRedditNotFoundException extends RuntimeException{
    public SubRedditNotFoundException(String message){
        super(message);
    }
}
