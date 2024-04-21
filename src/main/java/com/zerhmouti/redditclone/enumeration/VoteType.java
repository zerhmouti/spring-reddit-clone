package com.zerhmouti.redditclone.enumeration;

public enum VoteType {
    UP(1), DOWN(-1);

    private final int direction;
    VoteType(int direction) {
        this.direction=direction;
    }

    public Integer getDirection(){
        return direction;
    }
}
