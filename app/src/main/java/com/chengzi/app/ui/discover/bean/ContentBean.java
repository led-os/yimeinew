package com.chengzi.app.ui.discover.bean;

public class ContentBean {
    private String text;
    private String userId;  //  用户id
    private int userType;   //  // 1-普通用户 2-医生 3-咨询师 4-机构
    private int type;   //  0 普通文本 1 被@用户姓名

    public ContentBean(String text, String userId, int type) {
        this.text = text;
        this.userId = userId;
        this.type = type;
    }

    public ContentBean() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
