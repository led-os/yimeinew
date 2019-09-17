package com.handong.framework.base;

import com.google.gson.annotations.SerializedName;

public class ResponseBean<T> {

    private static final int SUCCESS_CODE = 1;
    private static final int STATUS_2 = 200;
    private static final int MULTIDEVICE_CODE = 602;    // 账号在其他设备登录
    private static final int ACCOUNT_FROZEN_CODE = 609; //  自己的账号被冻结，或删除
    private static final int TOKEN_EXPIRE_CODE = 403;   //  token 过期
    private static final int OTHER_FROZEN_CODE = 1001;  //  查看被人主页时，该主页的账号被删除或被冻结

    @SerializedName(value = "status",alternate = {"code"})
    private int status;
    @SerializedName(value = "message",alternate = {"msg"})
    private String message;
    private T data;

    public boolean isSuccess(){
        return SUCCESS_CODE == status || STATUS_2 == status;
    }

    public boolean isMultipeDevice(){
        return MULTIDEVICE_CODE == status;
    }

    public boolean isAccountFrozen(){
        return status == ACCOUNT_FROZEN_CODE;
    }

    public boolean isTokenExpire(){
        return status == TOKEN_EXPIRE_CODE;
    }

    public boolean isOtherForzen(){
        return status == OTHER_FROZEN_CODE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
