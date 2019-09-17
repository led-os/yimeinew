package com.chengzi.app.ui.mine.bean;

public class AgressmentBean {
    /**
     * id : 2
     * title : 2
     * content : 1
     * user_type : 4
     * info_type : 2
     */

    private String id;
    private String title;         //用户协议标题
    private String content;       //用户协议内容
    private String user_type;     //用户类型 必传 类型 1-用户 2-医生 3-咨询师 4-机构
    private String info_type;     //消息类型 必传 消息类型(1.协议;2.VIP;3.推广）

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getInfo_type() {
        return info_type;
    }

    public void setInfo_type(String info_type) {
        this.info_type = info_type;
    }
}