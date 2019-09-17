package com.chengzi.app.ui.pay.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPayFailureBindingImpl;

/**
 * 支付失败
 *
 * @ClassName:PayFailureActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/4/2 0002   14:29
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class PayFailureActivity extends BaseActivity<ActivityPayFailureBindingImpl, BaseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_failure;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding.tvSure.setOnClickListener(v -> {
            finish();
        });
    }
}
