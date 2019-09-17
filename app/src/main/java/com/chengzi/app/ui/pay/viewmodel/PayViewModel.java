package com.chengzi.app.ui.pay.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Pair;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.pay.bean.PaymentBean;
import com.chengzi.app.ui.pay.bean.VipPrePaymentBean;
import com.chengzi.app.ui.pay.model.PayService;

import java.util.ArrayList;
import java.util.List;

/**
 * 下单
 *
 * @ClassName:AccountDetailsViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class PayViewModel extends BaseViewModel {
    //获取支付信息-支付前
    public final MutableLiveData<List<UserOrderListBean>> getPayOrderLiveData = new MutableLiveData<>();
    public final MutableLiveData<Pair<Integer,String>> errLive = new MutableLiveData<>();
    //    //VIP预支付页面
//    public final MutableLiveData<ResponseBean<VipPrePaymentBean>> vipPrePaymentLiveData = new MutableLiveData<>();
//生成订单VIP
    public final MutableLiveData<ResponseBean<VipPrePaymentBean>> getOrderLiveData = new MutableLiveData<>();
    //Plan - 美人筹支付订单页面（冀）
    public final MutableLiveData<ResponseBean<VipPrePaymentBean>> planPayOrderLiveData = new MutableLiveData<>();

    public final MutableLiveData<PaymentBean> payLive = new MutableLiveData<>();

    private String payWay;  //  支付方式  支付宝 or 微信
    private String targetId;  //  通过此id 查询 支付信息, 获得prepaylog_id ，用 prepaylog_id 去 发起支付
    private String prepaylog_id;   //  用此id 发起支付

    private final PayService payService;

    public PayViewModel() {
        payService = Api.getApiService(PayService.class);
    }


    //购物车列表
    public void getPayOrder(List<String> order_id) {
        String token = AccountHelper.getToken();
        String user_id = AccountHelper.getUserId();
        payService.getPayOrder(token, user_id, order_id)
                .subscribe(new SimpleObserver<ResponseBean<List<UserOrderListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<UserOrderListBean>> pageBeanResponseBean) {
                        List<UserOrderListBean> list = pageBeanResponseBean.getData();
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        getPayOrderLiveData.postValue(list);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        errLive.postValue(new Pair<>(code,errorMsg));
                    }
                });
    }

    //VIP预支付页面
    public void getOrder(String vip_id) {
        payService.getVipPreOrder(AccountHelper.getToken(), AccountHelper.getUserId(), vip_id)
                .subscribe(new SimpleObserver<ResponseBean<VipPrePaymentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<VipPrePaymentBean> bean) {
                        getOrderLiveData.postValue(bean);
                    }
                });

    }

    //美人筹支付订单页面
    public void planPayOrder(String plan_order_id) {
        String token = AccountHelper.getToken();
        String user_id = AccountHelper.getUserId();
        payService.planPayOrder(token, user_id, plan_order_id)
                .subscribe(new SimpleObserver<ResponseBean<VipPrePaymentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<VipPrePaymentBean> bean) {
                        planPayOrderLiveData.postValue(bean);
                    }
                });
    }

    public void payment() {
        String paymentType = TextUtils.equals(payWay, Sys.wxPay) ? "wechat_app" : "ali_app";
        payService.payment(prepaylog_id, paymentType)
                .subscribe(new SimpleObserver<ResponseBean<PaymentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PaymentBean> paymentBeanResponseBean) {
                        payLive.postValue(paymentBeanResponseBean.getData());
                    }
                });
    }

    public void payNotify(){
        payService.payNotify(prepaylog_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPrepaylog_id() {
        return prepaylog_id;
    }

    public void setPrepaylog_id(String prepaylog_id) {
        this.prepaylog_id = prepaylog_id;
    }
}