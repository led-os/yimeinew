package com.chengzi.app.ui.goods.bean;

import java.io.Serializable;

public class PeopleSayQuestionBean implements Serializable {

    private static final long serialVersionUID = 8683452592189L;

    private String id;
    private String questioner_id;       //  提问者user id
    private String to_id;       // 针对主题，对应的ID，例如针对医生，则是user 表医生的user id
    private int type;           // 提问主题：0医生1咨询师2商品3机构
    private String content;     //  提问内容
    private String create_time;
    private String update_time;
    private int answers_count;      //  回答数量
    private PeopleSayAnswerBean first_answer;

    public PeopleSayQuestionBean(String id, String content, String create_time) {
        this.id = id;
        this.content = content;
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestioner_id() {
        return questioner_id;
    }

    public void setQuestioner_id(String questioner_id) {
        this.questioner_id = questioner_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time == null ? (create_time = "") : create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time == null ? (update_time = "") : update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getAnswers_count() {
        return answers_count;
    }

    public void setAnswers_count(int answers_count) {
        this.answers_count = answers_count;
    }

    public PeopleSayAnswerBean getFirst_answer() {
        return first_answer;
    }

    public void setFirst_answer(PeopleSayAnswerBean first_answer) {
        this.first_answer = first_answer;
    }

}
