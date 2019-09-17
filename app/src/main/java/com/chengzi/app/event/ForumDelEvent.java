package com.chengzi.app.event;

public class ForumDelEvent {

    private String forumId;

    public ForumDelEvent(String forumId) {
        this.forumId = forumId;
    }

    public String getForumId() {
        return forumId;
    }
}
