package com.chengzi.app.ui.find.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.DateUtils;

import java.io.Serializable;

public class ArticleManageBean implements Serializable {

    /**
     * id : 1
     * user_id : 1
     * title : 推荐in1222
     * content : <p>我是新增的</p>
     * type : 3
     * img :
     * create_time : 1555555555
     * update_time : 1558169461
     * delete_time : null
     */

    private String id;          // id
    private String user_id;     // 用户id
    private String title;       // 标题
    private String content;     // 内容
    private String type;        // 文章类型 1-人才招聘 2-行业会议 3-培训发布
    private String img;         // 图片
    private String create_time; // 创建时间
    private String update_time;
    private String delete_time;

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

    public String getCreate_times() {
        return DateUtils.dataTime(create_time);
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
