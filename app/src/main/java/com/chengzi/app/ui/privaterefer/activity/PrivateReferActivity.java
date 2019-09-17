package com.chengzi.app.ui.privaterefer.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPrivateReferBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.activity.vip.OpenVipActivity;
import com.chengzi.app.ui.privaterefer.viewmodel.PrivateReferViewModel;

/**
 * 私享咨询
 *
 * @ClassName:PrivateReferActivity
 * @PackageName:com.yimei.app.ui.privaterefer.activity
 * @Create On 2019/4/4 0004   10:15
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class PrivateReferActivity extends BaseActivity<ActivityPrivateReferBinding, PrivateReferViewModel> implements View.OnClickListener {

    public static void start(Context context) {
        BaseActivity activity = (BaseActivity) context;

        if (AccountHelper.shouldLogin(activity)) {
            return;
        }
        if (AccountHelper.getIdentity() > 1) {
            ToastUtils.showShort("只有普通用户可以私享咨询");
            return;
        }

        activity.showLoading("");
        PrivateReferViewModel viewModel = ViewModelProviders.of(activity).get(PrivateReferViewModel.class);
        viewModel.userInfoLiveData.observeForever(new Observer<UserInfoBean>() {
            @Override
            public void onChanged(@Nullable UserInfoBean infoBean) {
                if (infoBean == null) {
                    return;
                }
                viewModel.userInfoLiveData.setValue(null);
                viewModel.userInfoLiveData.removeObserver(this);
                activity.dismissLoading();
                if (infoBean == null) {
                    return;
                }
                int is_vip = infoBean.getIs_VIP();
                if (is_vip == 1) {
                    Intent intent = new Intent(context, PrivateReferActivity.class);
                    context.startActivity(intent);
                } else {
                    new MessageDialogBuilder(activity)
                            .setMessage("你还不是VIP，是否去购买？")
                            .setSureListener(v -> OpenVipActivity.start(activity))
                            .show();
                }
            }
        });
        viewModel.userInfo();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_private_refer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_private_refer:
                PrivateReferTypeActivity.start(this, Sys.TYPE_PRIVATE_REFER);
                break;
            case R.id.btn_diagnose_online:
                PrivateReferTypeActivity.start(this, Sys.TYPE_DIAGNOSE_ONLINE);
                break;
        }
    }
}
