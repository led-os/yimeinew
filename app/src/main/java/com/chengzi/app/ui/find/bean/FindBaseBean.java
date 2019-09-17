package com.chengzi.app.ui.find.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FindBaseBean<T> {

    @SerializedName(value = "category_id", alternate = {"id"})
    private String category_id;

    private String name;

    @SerializedName(value = "data", alternate = {"question", "goods_list"})
    private List<T> data;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getData() {
        return data == null ? (data = new ArrayList<>()) : data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
