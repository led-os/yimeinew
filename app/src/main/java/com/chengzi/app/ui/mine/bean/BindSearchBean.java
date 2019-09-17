package com.chengzi.app.ui.mine.bean;

public class BindSearchBean {

    /**
     * name : 北京三甲医院
     * id : 5
     * image :
     */

    private String id;
    private String name;        // 机构名称
    private String image;       // 机构头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
