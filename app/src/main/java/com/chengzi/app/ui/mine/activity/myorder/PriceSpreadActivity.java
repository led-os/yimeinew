package com.chengzi.app.ui.mine.activity.myorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPriceSpreadBindingImpl;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.event.AlipayEvent;
import com.chengzi.app.third.pay.Alipay;
import com.chengzi.app.third.pay.wxpay.WXPay;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.ui.pay.activity.PaySuccessActivity;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * 补差价
 * ①    -->普通用户 我的订单(列表和详情)
 *
 * @ClassName:PriceSpreadActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/4 0004   14:12
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */

public class PriceSpreadActivity extends BaseActivity<ActivityPriceSpreadBindingImpl, MyOrderListViewModel> {

    private String order_id;
    //订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）  3-->医院
    private int type;
    private SpreadsOrderBean data;

    public static void start(Context context, String order_id, int type) {
        context.startActivity(new Intent(context, PriceSpreadActivity.class)
                .putExtra("order_id", order_id)
                .putExtra("type", type)
        );
    }

    // 微信 支付宝
    private String mPayWay;
    public static final int REQUEST_CODE_ALIPAY = 0x01;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_price_spread;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(priceSpreadClickListener);

        EventBus.getDefault().register(this);

        SetLimitDialog.setPriceMode(mBinding.etMoney, 2);

        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", 1);
        mPayWay = Sys.wxPay;
        mBinding.ivWeixin.setSelected(true);

        //补差价下单
        mViewModel.spreadsOrderLiveData.observe(this, responseBean -> {
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
//                                PaySuccessActivity.start(this, data, type);
//                            } else {  //TODO:补差价支付失败后-不跳转失败页面-原型无体现-暂不显示
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
            PaySuccessActivity.start(this, data, type);
        } else {  //TODO:补差价支付失败后-不跳转失败页面-原型无体现-暂不显示
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
                String et_Money = mBinding.etMoney.getText().toString();
                if (TextUtils.isEmpty(mPayWay)) {
                    ToastUtils.showShort("请选择支付方式");
                    return;
                }
                if (TextUtils.isEmpty(et_Money)) {
                    ToastUtils.showShort("请输入补差金额");
                    return;
                }
                showLoading("");
                mViewModel.spreadsOrder(order_id, mPayWay, et_Money);
                break;
        }
    };
}