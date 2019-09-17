package com.chengzi.app.ui.goods.bean;

import java.io.Serializable;

public class PeopleSayAnswerBean implements Serializable {

    private static final long serialVersionUID = 8683452522189L;

    private String id;
    private String answerer_id;        //  回答用户ID
    private String to_question_id;     //  回答问题ID
    private String content;         //  回覆内容
    private String create_time;
    private String update_time;
    private String name;
    private String image;
    private boolean is_VIP;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerer_id() {
        return answerer_id;
    }

    public void setAnswerer_id(String answerer_id) {
        this.answerer_id = answerer_id;
    }

    public String getTo_question_id() {
        return to_question_id;
    }

    public void setTo_question_id(String to_question_id) {
        this.to_question_id = to_question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(boolean is_VIP) {
        this.is_VIP = is_VIP;
    }
}
