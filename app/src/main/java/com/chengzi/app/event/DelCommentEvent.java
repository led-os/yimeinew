package com.chengzi.app.event;

public class DelCommentEvent {

    private String target;  //  被评论的 案例|美人筹|帖子 id
    private String commentId;   //  评论id
    private int commentType;    //  评论类型  1 案例 2 美人筹 3 发现圈

    public DelCommentEvent(String target, int commentType) {
        this.target = target;
        this.commentType = commentType;
    }

    public DelCommentEvent(String target, String commentId, int commentType) {
        this.target = target;
        this.commentId = commentId;
        this.commentType = commentType;
    }

    public String getTarget() {
        return target;
    }

    public int getCommentType() {
        return commentType;
    }

    public String getCommentId() {
        return commentId;
    }
}
