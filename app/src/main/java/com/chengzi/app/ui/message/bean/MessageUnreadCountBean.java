package com.chengzi.app.ui.message.bean;

import com.google.gson.annotations.SerializedName;

public class MessageUnreadCountBean {

    private int system;
    @SerializedName("find-post-remind")
    private int findpostremind;
    @SerializedName("likes-comment")
    private int likescomment;
    private int fans;
    @SerializedName("quesition-online")
    private int quesitiononline;

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public int getFindpostremind() {
        return findpostremind;
    }

    public void setFindpostremind(int findpostremind) {
        this.findpostremind = findpostremind;
    }

    public int getLikescomment() {
        return likescomment;
    }

    public void setLikescomment(int likescomment) {
        this.likescomment = likescomment;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getQuesitiononline() {
        return quesitiononline;
    }

    public void setQuesitiononline(int quesitiononline) {
        this.quesitiononline = quesitiononline;
    }
}
