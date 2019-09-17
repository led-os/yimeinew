package com.chengzi.app.event;

public class DelChildCommentEvent {

    private String target;  //  被评论的 案例|美人筹|帖子 id
    private String parentId;    //  父评论id
    private int commentType;    //  评论类型  1 案例 2 美人筹 3 发现圈

    public DelChildCommentEvent(String target, String parentId, int commentType) {
        this.target = target;
        this.parentId = parentId;
        this.commentType = commentType;
    }

    public String getTarget() {
        return target;
    }

    public String getParentId() {
        return parentId;
    }

    public int getCommentType() {
        return commentType;
    }
}
