package com.chengzi.app.ui.mine.bean;


public class UserVipInfoBean {


    /**
     * user_id : 3
     * user_name : 阿姐
     * image : ["http://www.vue_admin.com/public/uploads/2019-04-12/5cb00bbd0bf3b.jpg"]
     * is_VIP : 0
     * grade : 8.0
     * bind_vip : 1
     * rank : 普通咨询师
     * occupation : 主治医师
     */

    private String id;     // 用户id
    private String name;   // 名称
    private String image;   // 头像
    private String type;   //   // 用户类型 1-用户 2-医生 3-咨询师 4-机构
    private boolean is_vip;      // 本身是否是vip 0 否 1是
    private String vip_endtime;       // VIP到期时间
    private boolean can_buy_vip;    // 是否可购买VIP
    private String level_name;     // 是否可购买VIP

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public String getVip_endtime() {
        return vip_endtime;
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
    }

    public boolean getCan_buy_vip() {
        return can_buy_vip;
    }

    public void setCan_buy_vip(boolean can_buy_vip) {
        this.can_buy_vip = can_buy_vip;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}
