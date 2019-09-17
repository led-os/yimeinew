package com.chengzi.app.ui.comment.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommentBean {

    @SerializedName(value = "comment_id", alternate = {"id"})
    private String id;      //  评论id

    private String from_uid;    //  评论者id

    @SerializedName(value = "cases_id", alternate = {"to_id"})
    private String to_id;       //   目标id，美人筹id，案例id，帖子id

    private String to_uid;      //    二级评论时，被回复人id
    private String content;     //     评论内容
    private String pid;         //     父评论id

    @SerializedName(value = "from_avatar", alternate = {"image", "user_image"})
    private String image;       //     评论者头像
    private String from_name;   //      评论者昵称

    private String to_name;     //  二级评论 被回复人昵称

    private int user_type;      //  用户类型 1 普通用户 2 医生 3 咨询师  4 医院
    private long create_time;   //  评论添加时间

    @SerializedName(value = "child_comment", alternate = {"sub_comment_list"})
    private List<CommentBean> child_comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public List<CommentBean> getChild_comment() {
        return child_comment == null ? (child_comment = new ArrayList<>()) : child_comment;
    }

    public List<CommentBean> getSubChildComment(){
        if (child_comment == null || child_comment.size() <= 3) {
            return getChild_comment();
        }
        if (subChildComment == null) {
            subChildComment = new ArrayList<>();
        }
        subChildComment.clear();
        int count = 0;
        for (CommentBean commentBean : child_comment) {
            subChildComment.add(commentBean);
            count ++;
            if (count >= 3) {
                break;
            }
        }
        return subChildComment;
    }

    private List<CommentBean> subChildComment;

    public void setChild_comment(List<CommentBean> child_comment) {
        this.child_comment = child_comment;
        subChildComment = null;
        if (onChildCommentChange != null) {
            onChildCommentChange.onChildCommentChange(this);
        }
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    private OnChildCommentChange onChildCommentChange;

    public void setOnChildCommentChange(OnChildCommentChange onChildCommentChange) {
        this.onChildCommentChange = onChildCommentChange;
    }

    public interface OnChildCommentChange {
        void onChildCommentChange(CommentBean bean);
    }
}
