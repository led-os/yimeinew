package com.chengzi.app.ui.mine.bean;

public class RuleListBean {

    /**
     * id : 11
     * title : 标题内容
     * content : 测试修改内容
     * type : 1
     * create_time : 2019-04-08 11:40
     * update_time : null
     * delete_time : null
     */

    private int id;
    private String title;
    private String content;
    private String type;           //1普通用户 2医生 3咨询师 4医院
    private String create_time;
    private String update_time;
    private String delete_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
