package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPayBinding;
import com.chengzi.app.event.AddRaiseEvent;
import com.chengzi.app.event.AlipayEvent;
import com.chengzi.app.event.OnBeautyPayStatusChangeEvent;
import com.chengzi.app.third.pay.Alipay;
import com.chengzi.app.third.pay.wxpay.WXPay;
import com.chengzi.app.ui.pay.bean.VipPrePaymentBean;
import com.chengzi.app.ui.pay.viewmodel.PayViewModel;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * 支付   购买VIP   医生我要推广的充值
 * ① -> 购买VIP
 * ② 美人筹(列表和详情)
 *
 * @ClassName:PayActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/4/2 0002   13:52
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */


public class PayActivity extends BaseActivity<ActivityPayBinding, PayViewModel> {

    public static final int REQUEST_CODE_ALIPAY = 0x01;

    private int type;

    /**
     * @param context
     * @param order_id VIP规格id 订单id
     * @param type     1购买VIP 2美人筹
     */
    public static void start(Context context, String order_id, int type) {
        context.startActivity(new Intent(context, PayActivity.class)
                .putExtra("order_id", order_id)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        EventBus.getDefault().register(this);

//        mViewModel.setPayWay(Sys.wxPay);
//        mBinding.ivWeixin.setSelected(true);

        String order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", -1);

        mViewModel.setTargetId(order_id);

        observe();

        // 普通用户 订单
        if (!TextUtils.isEmpty(order_id)) {
            showLoading(Sys.LOADING);
            if (type == 1) {
                mViewModel.getOrder(order_id);
            } else {
                mViewModel.planPayOrder(order_id);
            }
        }
    }

    private void observe() {
        //VIP预支付
        mViewModel.getOrderLiveData.observe(this, getVipOrderBean -> {
            dismissLoading();
            if (getVipOrderBean != null && getVipOrderBean.getData() != null) {
                VipPrePaymentBean bean = getVipOrderBean.getData();
                if (bean != null) {
                    mBinding.setBean(bean);
                    mViewModel.setPrepaylog_id(bean.getPrepaylog_id());
                }
            }
        });

        //美人筹预支付
        mViewModel.planPayOrderLiveData.observe(this, planPayOrderBean -> {
            dismissLoading();
            if (planPayOrderBean != null && planPayOrderBean.getData() != null) {
                VipPrePaymentBean bean = planPayOrderBean.getData();
                if (bean != null) {
                    mBinding.setBean(bean);
                    mViewModel.setPrepaylog_id(bean.getPrepaylog_id());
                }
            }
        });

        mViewModel.payLive.observe(this, paymentBean -> {

            dismissLoading();

            if (TextUtils.equals(mViewModel.getPayWay(), Sys.wxPay)) {

                WXPay.getInstance(this)
                        .startWeixinPay(paymentBean.getPartnerId(),
                                paymentBean.getPrepayId(), paymentBean.getNonceStr(), paymentBean.getSign());

            } else if (TextUtils.equals(mViewModel.getPayWay(), Sys.aliPay)) {

                Alipay alipay = new Alipay(this);
                alipay.payFromServer(paymentBean.getData());

            }

        });
    }

    @Subscribe
    public void onPayFinished(AlipayEvent event) {
        int payResult = event.getPayStatus();
        if (payResult == 1) {
            // 发起美人筹，支付成功后刷新美人筹列表
            EventBus.getDefault().post(new AddRaiseEvent());
            //  支付成功后，刷新 我的美人筹订单列表或订单详情
            EventBus.getDefault().post(new OnBeautyPayStatusChangeEvent());
            PayRaiseSuccessActivity.start(this, mBinding.getBean(), mViewModel.getTargetId(), type);
        } else {
            PayRaiseFailureActivity.start(this, mBinding.getBean());
        }
        mViewModel.payNotify();
        finish();
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {

        if (!ClickEvent.shouldClick(view)) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_weixin:  //微信
            case R.id.tv_weixin:
            case R.id.iv_weixin:
                mViewModel.setPayWay(Sys.wxPay);
                mBinding.ivWeixin.setSelected(true);
                mBinding.ivZhifubao.setSelected(false);
                break;
            case R.id.ll_zhifubao:  //支付宝
            case R.id.tv_zhifubao:
            case R.id.iv_zhifubao:
                mViewModel.setPayWay(Sys.aliPay);
                mBinding.ivWeixin.setSelected(false);
                mBinding.ivZhifubao.setSelected(true);
                break;
            case R.id.tv_pay:       //确认支付

                if (TextUtils.isEmpty(mViewModel.getPayWay())) {
                    ToastUtils.showShort("请选择支付方式");
                    return;
                }

                //  开始支付
                showLoading("");
                mViewModel.payment();

                break;
        }
    };
}