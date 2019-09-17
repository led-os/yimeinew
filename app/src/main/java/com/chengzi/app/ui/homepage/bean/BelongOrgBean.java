package com.chengzi.app.ui.homepage.bean;

import java.io.Serializable;

public class BelongOrgBean implements Serializable {


    private String id;
    private String name;
    private String image;
    private String address;
    private String telephone;
    private int is_VIP;
    private String grade;
    private float distance;

    public BelongOrgBean(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
