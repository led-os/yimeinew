package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

public class HomeArticleBean {

    /**
     * id : 1
     * user_id : 1
     * title : 推荐in1222
     * content : <p>我是新增的</p>
     * type : 3
     * img : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/2019/05/18/b4a60d0e1feb95aa4da41ab50e5c04cd.jpg
     * create_time : 1555555555
     * update_time : 1558169461
     * delete_time : null
     */

    private String id;
    private String user_id;
    private String title;
    private String content;
    private String type;
    private String img;
    private String create_time;
    private String update_time;
    private Object delete_time;

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

    public String getTypeName() {
//        1 - 人才招聘 2 - 行业会议 3 - 培训发布
//        return type;
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("1")) {
                return "[人才招聘]";
            } else if (type.equals("2")) {
                return "[行业会议]";
            } else if (type.equals("3")) {
                return "[培训发布]";
            }
        } else {
            return "[]";
        }
        return "[]";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Object getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Object delete_time) {
        this.delete_time = delete_time;
    }
}
