package com.chengzi.app.ui.home.bean;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.Navigator;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem implements Navigator,DropDownPopup.PopupBean {

    @SerializedName(value = "cateid", alternate = {"id"})
    private String id;
    private String parent_id;
    @SerializedName(value = "catename", alternate = {"name"})
    private String name;
    private int is_operation;

    @SerializedName(value = "secondaryClassification", alternate = {"threeClassification", "sub_list"})
    private List<CategoryItem> children;

    public CategoryItem() {
    }

    public CategoryItem(String id, String name) {
        setId(id);
        setName(name);
    }

    public CategoryItem(String name) {
        setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_operation() {
        return is_operation;
    }

    public void setIs_operation(int is_operation) {
        this.is_operation = is_operation;
    }

    public List<CategoryItem> getChildren() {
        return children == null ? (children = new ArrayList<>()) : children;
    }

    public void setChildren(List<CategoryItem> children) {
        this.children = children;
    }

    @Override
    public String getParam() {
        return getName();
    }

    @Override
    public String getValue() {
        return getId();
    }

    @Override
    public String getTypeText() {
        return getName();
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
