package com.chengzi.app.ui.mine.activity.popularize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityRechargeBindingImpl;
import com.chengzi.app.event.AlipayEvent;
import com.chengzi.app.third.pay.Alipay;
import com.chengzi.app.third.pay.wxpay.WXPay;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.viewmodel.IWantPopularizeViewModel;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 充值
 *
 * @ClassName:RechargeActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/12 0012   18:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */

public class RechargeActivity extends BaseActivity<ActivityRechargeBindingImpl, IWantPopularizeViewModel> {

    private String mPayWay;
    private SpreadsOrderBean data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        mBinding.setListener(priceSpreadClickListener);
        mPayWay = Sys.wxPay;
        mBinding.ivWeixin.setSelected(true);

        observe();
    }

    public static final int REQUEST_CODE_ALIPAY = 0x01;

    private void observe() {

        mViewModel.promotionPaymentLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.getData() != null) {
//                SpreadsOrderBean data = responseBean.getData();
//                finish();
//                PaySuccessActivity.start(this, data, type);
                //下单成功 调用支付宝和微信支付
                //  开始支付
                data = responseBean.getData();
                mViewModel.payment(mPayWay, data.getPrepaylog_id());
            } else {
                dismissLoading();
            }
        });
        //
        mViewModel.payLive.observe(this, paymentBean -> {

            dismissLoading();

            if (TextUtils.equals(mPayWay, Sys.wxPay)) {

                WXPay.getInstance(this)
                        .startWeixinPay(paymentBean.getPartnerId(),
                                paymentBean.getPrepayId(), paymentBean.getNonceStr(), paymentBean.getSign());

            } else if (TextUtils.equals(mPayWay, Sys.aliPay)) {

                Alipay alipay = new Alipay(this);
                alipay.payFromServer(paymentBean.getData());

//                startActivityForResult(new Intent(this, AlipayH5Activity.class)
//                                .putExtra(Sys.EXTRA, paymentBean.getRedirect_url())
//                                .putExtra("prepayId", data.getPrepaylog_id()),
//                        REQUEST_CODE_ALIPAY,
//                        data1 -> {
//                            int payResult = data1.getIntExtra("pay_result", 0);
//                            if (payResult == 1) {
////                                // 发起美人筹，支付成功后刷新美人筹列表
////                                EventBus.getDefault().post(new AddRaiseEvent());
////                                //  支付成功后，刷新 我的美人筹订单列表或订单详情
////                                EventBus.getDefault().post(new OnBeautyPayStatusChangeEvent());
////                                PayRaiseSuccessActivity.start(this, mBinding.getBean(), order_id, type);
//                                toast("充值成功!");
//                                finish();
//                            } else {
////                                PayRaiseFailureActivity.start(this, mBinding.getBean());
//                                toast("支付失败");
//                            }
//                            finish();
//                        });
            }
        });
    }

    @Subscribe
    public void onPayFinished(AlipayEvent event) {
        int payResult = event.getPayStatus();
        if (payResult == 1) {
            toast("充值成功!");
            finish();
        } else {
            toast("支付失败");
        }
        mViewModel.payNotify(data.getPrepaylog_id());
        finish();
    }

    private ClickEventHandler<Object> priceSpreadClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_weixin:  //微信
            case R.id.tv_weixin:
            case R.id.iv_weixin:
                mPayWay = Sys.wxPay;
                mBinding.ivWeixin.setSelected(true);
                mBinding.ivZhifubao.setSelected(false);
                break;
            case R.id.ll_zhifubao:  //支付宝
            case R.id.tv_zhifubao:
            case R.id.iv_zhifubao:
                mPayWay = Sys.aliPay;
                mBinding.ivWeixin.setSelected(false);
                mBinding.ivZhifubao.setSelected(true);
                break;
            case R.id.tv_pay:       //确认支付
                String money = mBinding.etMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    toast("请输入充值金额!");
                    break;
                }
                showLoading(Sys.LOADING);
                mViewModel.promotionPayment(money);
//                finish();
//                goActivity(mPayWay == PayConstants.wxPay ? PaySuccessActivity.class : PayFailureActivity.class);
                break;
        }
    };
}
