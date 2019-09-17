package com.chengzi.app.ui.vip.bean;

import com.chengzi.app.ui.home.bean.CategoryItem;

import java.util.List;

public class VipChannelIndexBean {

    /**
     * user : {"id":26,"name":"呆萌和画师、","headimg":"http://medicalbeauty.oss-cn-beijing.aliyuncs.com/155866593586726","mobile":"18310681301","telephone":null,"address":"北京市朝阳区建国路181号","grade":"8.0","skill_grade":"0.0","type":1,"occupation_name":"","consultant_name":"VIP咨询师","gender":0,"age":6,"vip_endtime":"2019/05/29 15:48","is_frozen":false,"is_vip":true,"is_auth":false,"is_auth_push":true,"is_auth_dis":false}
     */

    private List<CategoryItem> category;
    private VipUserInfoBean user;

    public List<CategoryItem> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryItem> category) {
        this.category = category;
    }

    public VipUserInfoBean getUser() {
        return user;
    }

    public void setUser(VipUserInfoBean user) {
        this.user = user;
    }
}
