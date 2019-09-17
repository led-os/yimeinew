package com.chengzi.app.ui.goods.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class BannerBean implements MultiItemEntity {


    private String url;             // 资源路径
    private String cover_image;     // 资源封面（图片同路径）
    private int type;               // 资源类型1-图片 2-视频

    public BannerBean() {
    }

    public BannerBean(String url, String cover_image, int type) {
        this.url = url;
        this.cover_image = cover_image;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return getType() - 1;
    }
}
