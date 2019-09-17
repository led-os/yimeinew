package com.chengzi.app.ui.onlinequestionandanswer.bean;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.ui.find.bean.FindBaseBean;

import java.util.List;

public class QAbean {

    @SerializedName(value = "id", alternate = {"question_id"})
    private String id;             // 主键id /问题id
    private String user_id;        // 用户id
    private String type;           // 问题类型（分类）
    private String title;       // 标题
    private String content;     // 内容
    private int status;          // 问答状态   1-已解决  2-未解决
    private String answer_id;       // 采纳id
    private String create_time;     // 创建时间
    private String update_time;
    private String delete_time;
    @SerializedName(value = "awser", alternate = {"toutal"})
    private int awser;           // 回答条数
    @SerializedName(value = "awser_content", alternate = {"select_answer"})
    private String awser_content;  //
    private int awser_user_id;       // 采纳用户id
    @SerializedName(value = "awser_user_name", alternate = {"select_name"})
    private String awser_user_name;  // 采纳用户名称
    @SerializedName(value = "awser_user_img", alternate = {"select_image"})
    private String awser_user_img;   // 采纳用户头像

    public QAbean(String title, int status,
                  int awser, String awser_user_name, String awser_user_img) {
        this.title = title;
        this.status = status;
        this.awser = awser;
        this.awser_user_name = awser_user_name;
        this.awser_user_img = awser_user_img;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
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

    public int getAwser() {
        return awser;
    }

    public void setAwser(int awser) {
        this.awser = awser;
    }

    public String getAwser_content() {
        return awser_content;
    }

    public void setAwser_content(String awser_content) {
        this.awser_content = awser_content;
    }

    public int getAwser_user_id() {
        return awser_user_id;
    }

    public void setAwser_user_id(int awser_user_id) {
        this.awser_user_id = awser_user_id;
    }

    public String getAwser_user_name() {
        return awser_user_name;
    }

    public void setAwser_user_name(String awser_user_name) {
        this.awser_user_name = awser_user_name;
    }

    public String getAwser_user_img() {
        return awser_user_img;
    }

    public void setAwser_user_img(String awser_user_img) {
        this.awser_user_img = awser_user_img;
    }

    public static class QAbeanWraper {
        private List<FindBaseBean<QAbean>> questionList;
        private NumBean num;

        public List<FindBaseBean<QAbean>> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<FindBaseBean<QAbean>> questionList) {
            this.questionList = questionList;
        }

        public NumBean getNum() {
            return num;
        }

        public void setNum(NumBean num) {
            this.num = num;
        }
    }

    public static class NumBean {
        private int resolved;
        private int unsolved;

        public int getResolved() {
            return resolved;
        }

        public void setResolved(int resolved) {
            this.resolved = resolved;
        }

        public int getUnsolved() {
            return unsolved;
        }

        public void setUnsolved(int unsolved) {
            this.unsolved = unsolved;
        }
    }
}
