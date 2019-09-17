package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPayRaiseFailureBinding;
import com.chengzi.app.ui.pay.bean.VipPrePaymentBean;
import com.chengzi.app.ui.pay.viewmodel.PayViewModel;

public class PayRaiseFailureActivity extends BaseActivity<ActivityPayRaiseFailureBinding, PayViewModel>
        implements View.OnClickListener {

    public static void start(Context context,  VipPrePaymentBean bean) {
        context.startActivity(new Intent(context, PayRaiseFailureActivity.class)
                .putExtra("paymentbean", bean)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_raise_failure;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VipPrePaymentBean bean = (VipPrePaymentBean) getIntent().getSerializableExtra("paymentbean");
        mBinding.setBean(bean);

        mBinding.setListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                finish();
                break;
        }
    }
}
