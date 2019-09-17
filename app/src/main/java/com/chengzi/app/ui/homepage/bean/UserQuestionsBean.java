package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import java.util.List;

public class UserQuestionsBean {

    /**
     * question_id : 5
     * title : 削骨
     * content : 美白也会有后遗症
     * type : 1
     * status : 2
     * create_time : 2018-03-28 15:30:22
     * answer_id : null
     * user_id : 1
     * name : 阿布
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=1.jpg
     * cate_name : 皮肤美容1
     * answer : [{"id":47,"question_id":5,"user_id":65,"content":"你是什么意思！这些是你给我们家孩子带来什么的时候才可以的、在线教育部门负责人称、","status":0,"create_time":"2019-05-20 18:22:03","update_time":null,"delete_time":null,"answer_user_name":"10","answer_user_image":"https://medicalbeauty.oss-cn-beijing.aliyuncs.com/ios1558582162762.jpg","user_type":2,"occupation_name":""}]
     * toutal : 1
     * answerNum : 1
     */

    private String question_id;// 问题uid
    private String title;      // 问题标题
    private String content;    // 问题内容
    private String type;       // 问题类型
    private String status;     // 问答状态  1-已解决  2-未解决
    private String create_time;// 创建时间
    private String answer_id;  // 被采纳者id
    private String user_id;    // 提问者id
    private String name;       // 提问者名字
    private String image;      // 提问者头像
    private String select_name;      //    #被采纳者名字
    private String select_image;     //    #被采纳者头像
    private String cate_name;  //
    private String toutal;     // 回答总数
    private String answerNum;  //
    private List<AnswerEntity> answer;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return !TextUtils.isEmpty(title) ? title : "";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return !TextUtils.isEmpty(status) ? status : "2";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getSelect_name() {
        return !TextUtils.isEmpty(select_name) ? select_name : "";
    }

    public void setSelect_name(String select_name) {
        this.select_name = select_name;
    }

    public String getSelect_image() {
        return select_image;
    }

    public void setSelect_image(String select_image) {
        this.select_image = select_image;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getToutal() {
        return !TextUtils.isEmpty(toutal) ? toutal : "0";
    }

    public void setToutal(String toutal) {
        this.toutal = toutal;
    }

    public String getAnswerNum() {
        return !TextUtils.isEmpty(answerNum) ? answerNum : "0";
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public List<AnswerEntity> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerEntity> answer) {
        this.answer = answer;
    }

    public static class AnswerEntity {
        /**
         * id : 47
         * question_id : 5
         * user_id : 65
         * content : 你是什么意思！这些是你给我们家孩子带来什么的时候才可以的、在线教育部门负责人称、
         * status : 0
         * create_time : 2019-05-20 18:22:03
         * update_time : null
         * delete_time : null
         * answer_user_name : 10
         * answer_user_image : https://medicalbeauty.oss-cn-beijing.aliyuncs.com/ios1558582162762.jpg
         * user_type : 2
         * occupation_name :
         */

        private String id;
        private String question_id;
        private String user_id;
        private String content;
        private String status;
        private String create_time;
        private String update_time;
        private String delete_time;
        private String answer_user_name;    // 被采纳者名字
        private String answer_user_image;   // 被采纳者头像
        private String user_type;
        private String occupation_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getAnswer_user_name() {
            return answer_user_name;
        }

        public void setAnswer_user_name(String answer_user_name) {
            this.answer_user_name = answer_user_name;
        }

        public String getAnswer_user_image() {
            return answer_user_image;
        }

        public void setAnswer_user_image(String answer_user_image) {
            this.answer_user_image = answer_user_image;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getOccupation_name() {
            return occupation_name;
        }

        public void setOccupation_name(String occupation_name) {
            this.occupation_name = occupation_name;
        }
    }
}
