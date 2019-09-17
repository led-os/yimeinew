package com.chengzi.app.ui.mine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySettingPhoneBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.MineViewModel;

/**
 * 设置电话(医院身份)
 *
 * @ClassName:SettingPhoneActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/11 0011   14:51
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */

public class SettingPhoneActivity extends BaseActivity<ActivitySettingPhoneBindingImpl, MineViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_setting_phone;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //top_bar 设置电话
        mBinding.topBar.setOnRightClickListener(v -> {
            String phone = mBinding.etPhone.getText().toString().trim();
            if (!TextUtils.isEmpty(phone)) {
                showLoading(Sys.LOADING);
                mViewModel.phoneSetting(phone);
            }
        });
        //设置座机电话
        mViewModel.phoneSettingLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                String phone = mBinding.etPhone.getText().toString().trim();
                //设置的电话
                AccountHelper.setTelephone(phone);
                toast("座机电话设置成功!");
                finish();
            }
        });
    }
}
