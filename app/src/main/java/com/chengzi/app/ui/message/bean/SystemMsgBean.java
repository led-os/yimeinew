package com.chengzi.app.ui.message.bean;

public class SystemMsgBean {

    private String id;
    private String type;
    private int jump_id;
    private String title;
    private String text;
    private String ticker;
    private int is_click;
    private int create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getJump_id() {
        return jump_id;
    }

    public void setJump_id(int jump_id) {
        this.jump_id = jump_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getIs_click() {
        return is_click;
    }

    public void setIs_click(int is_click) {
        this.is_click = is_click;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
