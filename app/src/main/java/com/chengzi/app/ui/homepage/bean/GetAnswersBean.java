package com.chengzi.app.ui.homepage.bean;

public class GetAnswersBean {

    private String answer_id;
    private String answerer_id;
    private String to_question_id;
    private String answer_content;
    private String question_content;
    private int answer_num;
    private String update_time;

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
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

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public int getAnswer_num() {
        return answer_num;
    }

    public void setAnswer_num(int answer_num) {
        this.answer_num = answer_num;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
