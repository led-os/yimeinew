package com.chengzi.app.ui.seckill.bean;

public class KillTimeBean {

    /**
     * num : 16
     * time : 16:00
     * status : 1
     */

    private String num;     // 小时
    private String time;    // 小时：分钟
    private String status;  // 1-进行中 2-即将开始

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_Name() {
//        return status;
        if (status != null) {
            if (status.equals("1")) {
                return "抢购中";
            }
        }
        return "即将开抢";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
