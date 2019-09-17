package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import java.util.List;

public class UserRecordListBean {

    /**
     * id : 13
     * user_id : 47
     * start_time : 2019-04-28
     * end_time : 2019-05-28
     * content : 哈哈哈哈哈哈哈哈啊
     * image : null
     * doccertificate_image : null
     * aptitude_image : null
     * card_image : null
     * create_time : 1559009327
     * update_time : null
     * delete_time : null
     */

    private String id;
    private String user_id;
    private String start_time;
    private String end_time;
    private String content;
    private List<String> image;
    private String create_time;


    public String getTime() {
        String start = !TextUtils.isEmpty(start_time) ? start_time : "";
        String end = !TextUtils.isEmpty(end_time) ? end_time : "";
        return start + "\n至\n" + end;
    }

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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getContent() {
        return !TextUtils.isEmpty(content) ? content : "";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}