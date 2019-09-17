package com.chengzi.app.third.pay;

/**
 * @ClassName:com.tianyige.app.constants
 * @Create On 2017/12/11 0011
 * @author:yanguangzhuang
 */

public class PayConstants {

    public static String WX_APP_ID = "wx81f3cd1780d24bc7";
    public static String WX_API_KEY = "88888888888888888888888888888888";

    public static final String PAY_SUCCESS = "paySuccess";

    public static boolean isPay = false;
    /**
     * 支付方式(类别)：101 、微信 102、支付宝 103余额
     *
     * @param context
     * @param params
     * @param callback
     */
    public static final String WXPAY = "101";
    public static final String ALIPAY = "102";
    public static final String YEPAY = "103";


}