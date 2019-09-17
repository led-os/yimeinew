package com.chengzi.app.third.pay.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Desc:
 * @ClassName:WXEntity
 * @PackageName:com.qihuang.app.common.pay.bean
 * @Create On 2019/2/27 0027
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class WXEntity {


    /**
     * package : Sign=WXPay
     * paySign : E1545AAC905A8CE5CC2673A2938EFF87
     * appid : wx91327092f a602b76
     * prepayid : wx221701196796693ecd1d245f1562919528
     * partnerid : 1528865071
     * nonc estr : 8vx0nyu4pjjh3rhcta4vjefwxa3bmdza
     * timestamp : 1553245122
     */

    @SerializedName("package")
    private String packageX;
    private String paySign;
    private String appid;
    private String prepayid;
    private String partnerid;
    private String noncestr; // FIXME check this code
    private int timestamp;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr == null ? "" : noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
