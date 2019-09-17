package com.chengzi.app.ui.mine.bean;

public class BankCityBean {

    /**
     * id : 14
     * pid : 0
     * name : 北京
     */

    private String id;    // 省/市 id
    private String pid;   // 上一级id
    private String name;  // 省市名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
