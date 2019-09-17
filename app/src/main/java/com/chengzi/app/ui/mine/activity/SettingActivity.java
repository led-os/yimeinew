package com.chengzi.app.ui.mine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.autoupdata.CheckVersion;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySettingBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.UpdatePasswordEvent;
import com.chengzi.app.ui.mine.viewmodel.SettingViewModel;
import com.chengzi.app.utils.CacheManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 设置
 *
 * @ClassName:SettingActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/8 0008   11:54
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class SettingActivity extends BaseActivity<ActivitySettingBindingImpl, SettingViewModel> {

    private int onlineStatus;//在线状态状( 1-在线 2-离线 3-忙碌)

    @Override
    public int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        mBinding.setListener(setClickListener);
        //消息提醒 只有普通用户显示
        mBinding.llMessageFree.setVisibility(AccountHelper.getIdentity() == 1 ? View.VISIBLE : View.GONE);
        //在线情况 只有普通用户不显
        mBinding.rgButton.setVisibility(AccountHelper.getIdentity() == 1 ? View.GONE : View.VISIBLE);

        onlineStatus = AccountHelper.getOLStatus();

        resultData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
        mBinding.tvMessageFree.setSelected(onlineStatus == SettingViewModel.STATE_ONLINE ? true : false);
        if (onlineStatus == 0) {
            mBinding.rbSetLeft.setChecked(false);
            mBinding.rbSetCenter.setChecked(false);
            mBinding.rbSetRight.setChecked(false);
        }
        if (!AccountHelper.isLogin()) {
            mBinding.elPwd.setContent("");
        }
        //设置缓存
        try {
            mBinding.elClearCache.setContent(CacheManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        /**
         * 设置用户在线状态 //在线状态状( 1-在线 2-离线 3-忙碌)
         */
        switch (onlineStatus) {
            case SettingViewModel.STATE_ONLINE:
                mBinding.rbSetLeft.setChecked(true);
                break;
            case SettingViewModel.STATE_OFFLINE:
                mBinding.rbSetCenter.setChecked(true);
                break;
            case SettingViewModel.STATE_BUSY:
                mBinding.rbSetRight.setChecked(true);
                break;
        }

        //设置手机号
        mBinding.elMobile.setContent(TextUtils.isEmpty(AccountHelper.getMobile()) ? "" : AccountHelper.getMobile().substring(0, 3) + "****" + AccountHelper.getMobile().substring(7));
    }

    private void resultData() {
        mViewModel.responseBeanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                toast("操作成功");
                AccountHelper.setOLStatus(onlineStatus);
                mBinding.tvMessageFree.setSelected(onlineStatus == SettingViewModel.STATE_ONLINE ? true : false);
            }
        });
    }

    @Subscribe
    public void onUpdatePassword(UpdatePasswordEvent event){
        finish();
    }

    private ClickEventHandler<Object> setClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_message_free:  //信息提醒-->只有普通用户有  1-推送 其他-关闭
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
//                mBinding.tvMessageFree.setSelected(AccountHelper.getOLStatus() == SettingViewModel.STATE_ONLINE ? true : false);
                showLoading(Sys.LOADING);
                if (onlineStatus == SettingViewModel.STATE_ONLINE) {  //如果是推送则关闭 如果关闭则开启
                    mViewModel.changeOnlineState(SettingViewModel.STATE_OFFLINE);
                    onlineStatus = SettingViewModel.STATE_OFFLINE;
                } else {
                    mViewModel.changeOnlineState(SettingViewModel.STATE_ONLINE);
                    onlineStatus = SettingViewModel.STATE_ONLINE;
                }
                break;
            case R.id.rb_set_left:      //在线状态——>在线-->医生 咨询师 医院
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                onlineStatus = SettingViewModel.STATE_ONLINE;
                mViewModel.changeOnlineState(SettingViewModel.STATE_ONLINE);
                break;
            case R.id.rb_set_center:    //在线状态——>离线-->医生 咨询师 医院
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                onlineStatus = SettingViewModel.STATE_OFFLINE;
                mViewModel.changeOnlineState(SettingViewModel.STATE_OFFLINE);
                break;
            case R.id.rb_set_right:     //在线状态——>忙碌-->医生 咨询师 医院
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                onlineStatus = SettingViewModel.STATE_BUSY;
                mViewModel.changeOnlineState(SettingViewModel.STATE_BUSY);
                break;
            case R.id.tv_check_update:     //检查更新
                CheckVersion.update(this, Api.getBaseUrl() + "android-version", true);
                break;
            case R.id.el_clear_cache:     //清除缓存
                new MessageDialogBuilder(this)
                        .setMessage("确定要清除缓存吗？")
                        .setSureListener(v1 -> {
                            CacheManager.clearAllCache(SettingActivity.this);
                            String totalCacheSize = "";
                            try {
                                totalCacheSize = CacheManager.getTotalCacheSize(SettingActivity.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mBinding.elClearCache.setContent(totalCacheSize + "");
                            ToastUtils.showShort("清除成功");
                        }).show();
                break;
            case R.id.el_mobile:     //手机号
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                UpdatePhoneAndPwdActivity.start(this, 1);
                break;
            case R.id.el_pwd:     //密码
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                UpdatePhoneAndPwdActivity.start(this, 2);
                break;
            case R.id.tv_un_login:     //退出登录]
                AccountHelper.logout();
                MainActivity.isSetMine = true;
                finish();
                break;
        }
    };
}