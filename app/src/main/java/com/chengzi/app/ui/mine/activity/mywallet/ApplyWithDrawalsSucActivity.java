package com.chengzi.app.ui.mine.activity.mywallet;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityApplyWithDrawalsSucBindingImpl;

/**
 * 提现成功
 *
 * @ClassName:ApplyWithDrawalsSucActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   16:43
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class ApplyWithDrawalsSucActivity extends BaseActivity<ActivityApplyWithDrawalsSucBindingImpl, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_apply_with_drawals_suc;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding.setListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure://确定
                finish();
                goActivity(MyWalletActivity.class);
                break;
        }
    }
}