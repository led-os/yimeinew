package com.chengzi.app.ui.mine.bean;

import java.io.Serializable;

public class PromotionSelectProductBean implements Serializable {

    /**
     * id : 13
     * name : 玻尿酸祛痘
     * buy_price : 299.00
     * spell_price : 1991.00
     * point : 9.0
     * image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/2019/06/11/252a48a4ddaf241bce6a823e638314b9.jpg
     * buy_count : 2
     * canUseToPromotion : true
     * canChoose : true
     */

    private String id;
    private String name;
    private String buy_price;
    private String spell_price;
    private String point;
    private String image;
    private String buy_count;
    private boolean canUseToPromotion;
    private boolean canChoose;

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

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getSpell_price() {
        return spell_price;
    }

    public void setSpell_price(String spell_price) {
        this.spell_price = spell_price;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(String buy_count) {
        this.buy_count = buy_count;
    }

    public boolean isCanUseToPromotion() {
        return canUseToPromotion;
    }

    public void setCanUseToPromotion(boolean canUseToPromotion) {
        this.canUseToPromotion = canUseToPromotion;
    }

    public boolean isCanChoose() {
        return canChoose;
    }

    public void setCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
    }
}
