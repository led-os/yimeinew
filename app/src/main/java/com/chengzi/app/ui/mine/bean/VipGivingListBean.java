package com.chengzi.app.ui.mine.bean;

public class VipGivingListBean {

    /**
     * id : 2
     * receivemobile : 13241342556
     * create_time : 2019-04-10 18:33
     * receiveimage : www.vue_admin.com["\/public\/uploads\/2019-04-15\/5cb3ed045e351.jpg"]
     * receivename : 阿布
     * receiveuid : null
     */

    private String id;              // 转赠记录id
    private String receivemobile;   // 接收人手机号
    private String create_time;     // 转赠时间
    private String receiveimage;    // 接收人头像
    private String receivename;     // 接受者姓名
    private String receiveuid;      // 接收人用户id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivemobile() {
        return receivemobile;
    }

    public void setReceivemobile(String receivemobile) {
        this.receivemobile = receivemobile;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReceiveimage() {
        return receiveimage;
    }

    public void setReceiveimage(String receiveimage) {
        this.receiveimage = receiveimage;
    }

    public String getReceivename() {
        return receivename;
    }

    public void setReceivename(String receivename) {
        this.receivename = receivename;
    }

    public String getReceiveuid() {
        return receiveuid;
    }

    public void setReceiveuid(String receiveuid) {
        this.receiveuid = receiveuid;
    }
}
