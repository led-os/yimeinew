package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserIndexBean {

    private String id;
    private String user_id;         // 发布人id
    private String content;         // 内容
    private String likes_num;       // 点赞数
    private String comment_count;   // 评论数
    private String collection_num;  // 收藏数
    private String forward_num;     // 转发数
    private String type;            // 身份类型  类型 1-用户 2-医生 3-咨询师 4-机构
    @SerializedName(value = "title")
    private String tiele;           // 身份头衔 ->
    private String username;        // 用户名
    private String autograph;       // 签名
    private String is_follow;       // 是否关注  0 否  1 是
    private String at_user_id;
    private String resource_type;   // 资源类型  1 图片   2视频
    private List<String> pics;      // 图片
    private String video;           // 视频
    private String create_time;
    private String update_time;
    private String delete_time;
    private String image;           // 头像
    private String time;            // 距离发帖时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes_num() {
        return !TextUtils.isEmpty(likes_num) ? likes_num : "0";
    }

    public void setLikes_num(String likes_num) {
        this.likes_num = likes_num;
    }

    public String getCollection_num() {
        return !TextUtils.isEmpty(collection_num) ? collection_num : "0";
    }

    public void setCollection_num(String collection_num) {
        this.collection_num = collection_num;
    }

    public String getForward_num() {
        return !TextUtils.isEmpty(forward_num) ? forward_num : "0";
    }

    public void setForward_num(String forward_num) {
        this.forward_num = forward_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAt_user_id() {
        return at_user_id;
    }

    public void setAt_user_id(String at_user_id) {
        this.at_user_id = at_user_id;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getComment_count() {
        return !TextUtils.isEmpty(comment_count) ? comment_count : "0";
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getTiele() {
        return !TextUtils.isEmpty(tiele) ? tiele : "";
    }

    public void setTiele(String tiele) {
        this.tiele = tiele;
    }

    public String getUsername() {
        return !TextUtils.isEmpty(username) ? username : "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return !TextUtils.isEmpty(time) ? time : "";
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
