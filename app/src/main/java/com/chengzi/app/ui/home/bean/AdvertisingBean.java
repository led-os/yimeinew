package com.chengzi.app.ui.home.bean;

import java.util.List;

public class AdvertisingBean {

   
    private String id;
    private String class_id;
    private int type;
    private String title;
    private String url;
    private String image;
    private int status;
    private int create_time;
    private String update_time;
    private String dalete_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDalete_time() {
        return dalete_time;
    }

    public void setDalete_time(String dalete_time) {
        this.dalete_time = dalete_time;
    }

    public static class AdvertisingHolderBean{

        private List<AdvertisingBean> wholeData;
        private List<AdvertisingBean> thirdData;

        public List<AdvertisingBean> getWholeData() {
            return wholeData;
        }

        public void setWholeData(List<AdvertisingBean> wholeData) {
            this.wholeData = wholeData;
        }

        public List<AdvertisingBean> getThirdData() {
            return thirdData;
        }

        public void setThirdData(List<AdvertisingBean> thirdData) {
            this.thirdData = thirdData;
        }
    }
}
