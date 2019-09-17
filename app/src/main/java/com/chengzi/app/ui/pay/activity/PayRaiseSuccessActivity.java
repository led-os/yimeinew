package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPayRaiseSuccessBinding;
import com.chengzi.app.event.BuyVipEvent;
import com.chengzi.app.ui.mine.activity.order_beauty_raise.BeautyRaiseDetailsActivity;
import com.chengzi.app.ui.pay.bean.VipPrePaymentBean;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;

import org.greenrobot.eventbus.EventBus;

public class PayRaiseSuccessActivity extends BaseActivity<ActivityPayRaiseSuccessBinding, BaseViewModel>
        implements View.OnClickListener {

    public static void start(Context context, VipPrePaymentBean bean, String orderId, int type) {
        context.startActivity(new Intent(context, PayRaiseSuccessActivity.class)
                .putExtra("paymentbean", bean)
                .putExtra("orderId", orderId)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_raise_success;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VipPrePaymentBean bean = (VipPrePaymentBean) getIntent().getSerializableExtra("paymentbean");
        mBinding.setBean(bean);
        mBinding.setListener(this);

        int type = getIntent().getIntExtra("type", 0);
        mBinding.tvSure.setText(type == 0 ? "查看订单" : "确定");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:      //  查看订单详情
                int type = getIntent().getIntExtra("type", 0);
                if (type == 0) {    //  美人筹订单跳到订单详情，购买vip 直接返回
                    String orderId = getIntent().getStringExtra("orderId");
                    BeautyRaiseDetailsActivity.start(this, orderId, null, 1, 0);
                } else if (type == 1) {
                    EventBus.getDefault().post(new BuyVipEvent());
                }
                finish();
                break;
        }
    }
}
