package com.chengzi.app.ui.home.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomeRecommandBean<T> {

    private int id;
    private String name;
    @SerializedName(value = "goodsLists", alternate = {"goods_list"})
    private List<T> goodsLists;
    private int type;

    public HomeRecommandBean() {
    }

    public HomeRecommandBean(String name, List<T> goodsLists, int type) {
        this.name = name;
        this.goodsLists = goodsLists;
        this.type = type;
    }

    public HomeRecommandBean(String name, List<T> goodsLists) {
        this.name = name;
        this.goodsLists = goodsLists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getGoodsLists() {
        return goodsLists == null ? (goodsLists = new ArrayList<>()) : goodsLists;
    }

    public void setGoodsLists(List<T> goodsLists) {
        this.goodsLists = goodsLists;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
