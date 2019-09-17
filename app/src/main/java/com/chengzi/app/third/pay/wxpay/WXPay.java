package com.chengzi.app.third.pay.wxpay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseLife;
import com.handong.framework.base.ResponseBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.chengzi.app.third.pay.MD5;
import com.chengzi.app.third.pay.PayConstants;
import com.chengzi.app.third.pay.bean.WXEntity;
import com.chengzi.app.third.pay.model.PayDataService;
import com.chengzi.app.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Desc:微信支付
 * @ClassName:WXPay
 * @PackageName:com.qihuang.app.common.pay
 * @Create On 2019/2/27 0027
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class WXPay {


    private Activity mActivity;


    private static WXPay ourInstance;

    private WXPay() {
    }

    public static WXPay getInstance(Activity mActivity) {
        if (ourInstance == null) {
            ourInstance = new WXPay(mActivity);
        }
        return ourInstance;
    }

    public void startWeixinPay(String partnerId, String prepayId, String nonce, String sign) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mActivity, WXPayEntryActivity.WX_ID, false);
        iwxapi.registerApp(WXPayEntryActivity.WX_ID);
        PayReq payReq = new PayReq();

        payReq.appId = WXPayEntryActivity.WX_ID;
        payReq.partnerId = partnerId;     //商户号
        payReq.prepayId = prepayId;     //  微信预下单id
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = nonce;
        payReq.timeStamp = genTimeStamp() + "";
//        List<Pair<String, String>> signParams = new LinkedList<Pair<String, String>>();
//        signParams.add(new Pair("appid", payReq.appId));
//        signParams.add(new Pair("noncestr", payReq.nonceStr));
//        signParams.add(new Pair("package", payReq.packageValue));
//        signParams.add(new Pair("partnerid", payReq.partnerId));
//        signParams.add(new Pair("prepayid", payReq.prepayId));
//        signParams.add(new Pair("timestamp", payReq.timeStamp));

        payReq.sign = sign;
        iwxapi.sendReq(payReq);
    }


    public WXPay(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * @param prescriptionId 系统订单号
     */
    public void getOrderInfo(String prescriptionId) {

        SharedPreferences sp = mActivity.getSharedPreferences("WXOrderId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("prescriptionId", prescriptionId);
        editor.commit();


        Map<String, String> params = new HashMap<>();
        params.put("prescriptionId", prescriptionId);
        Api.getApiService(PayDataService.class).getPayInfo(params).compose(BaseLife.getObservableScheduler())
                .subscribe(new Observer<ResponseBean<WXEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBean<WXEntity> wxEntityResponseBean) {
                        pay(wxEntityResponseBean.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void pay(WXEntity data) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mActivity, PayConstants.WX_APP_ID, false);
        iwxapi.registerApp(PayConstants.WX_APP_ID);
        PayReq payReq = new PayReq();

        payReq.appId = PayConstants.WX_APP_ID;
        payReq.partnerId = data.getPartnerid();
        payReq.prepayId = data.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = data.getNoncestr();
        payReq.timeStamp = data.getTimestamp() + "";

        List<Pair<String, String>> signParams = new LinkedList<Pair<String, String>>();
        signParams.add(new Pair("appid", payReq.appId));
        signParams.add(new Pair("noncestr", payReq.nonceStr));
        signParams.add(new Pair("package", payReq.packageValue));
        signParams.add(new Pair("partnerid", payReq.partnerId));
        signParams.add(new Pair("prepayid", payReq.prepayId));
        signParams.add(new Pair("timestamp", payReq.timeStamp));

        payReq.sign = data.getPaySign();
        Log.e("WXPAY", "checkArgs=" + payReq.checkArgs());
        iwxapi.sendReq(payReq);

    }

    private String genAppSign(List<Pair<String, String>> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).first);
            sb.append('=');
            sb.append(params.get(i).second);
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WXPayEntryActivity.WX_KEY);          // API_KEY
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        System.out.println("appSign--" + appSign);
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}
