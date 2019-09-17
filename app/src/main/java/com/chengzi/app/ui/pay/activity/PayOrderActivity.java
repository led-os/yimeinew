package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PayCarOrderAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPayOrderBinding;
import com.chengzi.app.dialog.PayDialog;
import com.chengzi.app.event.AlipayEvent;
import com.chengzi.app.event.UserOrderEvent;
import com.chengzi.app.event.UserOrderIdEvent;
import com.chengzi.app.third.pay.Alipay;
import com.chengzi.app.third.pay.wxpay.WXPay;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.pay.viewmodel.PayViewModel;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.dialog.TokenDialog;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单支付-->多个订单-->暂时订单同意调用这个页面吧!!!!!
 * <p>
 * 获取确认支付信息-->根据order_id
 * ① 购物车中 确认下单
 * ② 订单中的 付款
 *
 * @ClassName:PayOrderActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/4/28 0028   18:19
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/28 0028 handongkeji All rights reserved.
 */
//MyShopCarViewModel
public class PayOrderActivity extends BaseActivity<ActivityPayOrderBinding, PayViewModel> {

    public static final int REQUEST_CODE_ALIPAY = 0x01;

    //选中的order_id
    private List<String> order_id;

    //购物车-->type=1(支付在最下面)  默认都是-1(支付在订单下边)
    private int type;
    private PayCarOrderAdapter adapter;

    public static void start(Context context, List<String> order_id) {
        context.startActivity(new Intent(context, PayOrderActivity.class)
                .putStringArrayListExtra("order_id", (ArrayList<String>) order_id)
        );
    }

    public static void start(Context context, int type, List<String> order_id) {
        context.startActivity(new Intent(context, PayOrderActivity.class)
                .putExtra("type", type)
                .putStringArrayListExtra("order_id", (ArrayList<String>) order_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        mBinding.setListener(clickListener);
        order_id = getIntent().getStringArrayListExtra("order_id");
        type = getIntent().getIntExtra("type", -1);
        if (type == 1) {    //购物车
            mBinding.tvConfirmPayment.setVisibility(View.VISIBLE);
            mBinding.llOrderPayType.setVisibility(View.GONE);
        } else {
            mBinding.tvConfirmPayment.setVisibility(View.GONE);
            mBinding.llOrderPayType.setVisibility(View.VISIBLE);
        }

        initRecyclerView();

        if (order_id != null && order_id.size() > 0) {
            showLoading(Sys.LOADING);
            mViewModel.getPayOrder(order_id);
        }

        observe();

    }

    private void observe() {
        //购物车/订单 order_id-->获取支付信息
        mViewModel.getPayOrderLiveData.observe(this, userOrderListBean -> {
            dismissLoading();
            if (userOrderListBean != null && userOrderListBean.size() > 0) {
                //展示 支付的订单信息
                String prepaylog_id = userOrderListBean.get(0).getPrepaylog_id();
                mViewModel.setPrepaylog_id(prepaylog_id);
                adapter.setNewData(userOrderListBean);
            }
        });

        mViewModel.errLive.observe(this, pair -> {
            if (pair.first == 501) {
                TokenDialog tokenDialog = new TokenDialog();
                tokenDialog.setMessage(pair.second);
                tokenDialog.setConfirmListener(v -> {
                    finish();
                });
                tokenDialog.show(getSupportFragmentManager(), "errLive");
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

//                startActivityForResult(new Intent(this, AlipayH5Activity.class)
//                                .putExtra(Sys.EXTRA, paymentBean.getRedirect_url())
//                                .putExtra("prepayId", mViewModel.getPrepaylog_id()),
//                        REQUEST_CODE_ALIPAY,
//                        data -> {
//                            int payResult = data.getIntExtra("pay_result", 0);
//                            EventBus.getDefault().postSticky(new UserOrderEvent(adapter.getData()));
//                            EventBus.getDefault().postSticky(new UserOrderIdEvent(order_id));
//                            if (payResult == 1) {
//                                PayOrderSuccessActivity.start(this);
//                            } else {
//                                PayOrderFailureActivity.start(this);
//                            }
//                            finish();
//                        });

            }

        });

    }

    @Subscribe
    public void onPayFinished(AlipayEvent event) {
        int payResult = event.getPayStatus();
        EventBus.getDefault().postSticky(new UserOrderEvent(adapter.getData()));
        EventBus.getDefault().postSticky(new UserOrderIdEvent(order_id));
        if (payResult == 1) {
            PayOrderSuccessActivity.start(this);
        } else {
            PayOrderFailureActivity.start(this);
        }
        mViewModel.payNotify();
        finish();
    }

    private void initRecyclerView() {
        adapter = new PayCarOrderAdapter();
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.mRecyclerView.setAdapter(adapter);
    }

    private ClickEventHandler<UserOrderListBean> clickListener = (view, bean) -> {

        if (!ClickEvent.shouldClick(view)) {
            return;
        }

        switch (view.getId()) {
            case R.id.tv_confirm_payment:  //  确认支付
                new PayDialog(this)
                        .setPayWXListener(v -> {
                            mViewModel.setPayWay(Sys.wxPay);
                            //  开始支付
                            showLoading("");
                            mViewModel.payment();
                        })
                        .setPayAliListener(v -> {
                            mViewModel.setPayWay(Sys.aliPay);
                            //  开始支付
                            showLoading("");
                            mViewModel.payment();
                        })
                        .show();
                break;
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

//                goActivity(mPayWay == Sys.wxPay ? PaySuccessActivity.class : PayFailureActivity.class);
                break;
        }
    };
}
