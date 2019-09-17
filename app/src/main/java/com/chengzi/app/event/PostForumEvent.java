package com.chengzi.app.event;

public class PostForumEvent {

    private int userType;

    public PostForumEvent(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }
}
