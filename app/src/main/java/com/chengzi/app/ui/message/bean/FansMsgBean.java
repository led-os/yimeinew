package com.chengzi.app.ui.message.bean;

import android.text.TextUtils;

public class FansMsgBean {

    private String id;
    private String username;
    private String usericon;
    private String create_time;
    private String level;
    private int user_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
    public long getTime(){
        if (TextUtils.isEmpty(create_time)) {
            return 0;
        }
        return Long.valueOf(create_time);
    }
}
