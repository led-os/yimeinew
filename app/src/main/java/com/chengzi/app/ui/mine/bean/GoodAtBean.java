package com.chengzi.app.ui.mine.bean;

import java.io.Serializable;

public class GoodAtBean implements Serializable {
    private String id;      // 分类id
    private String name;    // 分类name
    private boolean ischoose = false;

    public GoodAtBean(String id, String name, boolean ischoose) {
        this.id = id;
        this.name = name;
        this.ischoose = ischoose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return ischoose;
    }

    public void setChoose(boolean choose) {
        ischoose = choose;
    }
}
