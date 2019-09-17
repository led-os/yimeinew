package com.chengzi.app.event;

public class AddCommentEvent {

    private String target;  //  被评论的 案例|美人筹|帖子 id
    private int commentType;    //  评论类型  1 案例 2 美人筹 3 发现圈

    public AddCommentEvent(String target, int commentType) {
        this.target = target;
        this.commentType = commentType;
    }

    public String getTarget() {
        return target;
    }

    public int getCommentType() {
        return commentType;
    }
}
