package com.chengzi.app.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chengzi.app.BuildConfig;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentInviteFriendsBindingImpl;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.ui.mine.bean.GetDownloadUrlQrcodeBean;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.uits.ImageLoader;

/**
 * 邀请好友fragment
 */
public class InviteFriendsFragment extends BaseFragment<FragmentInviteFriendsBindingImpl, MyVipViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_invite_friends;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //getDownloadUrlQrcode
        viewModel.getDownloadUrlQrcode();
        viewModel.getDownloadUrlQrcodeLiveData.observe(this, getDownloadUrlQrcodeBean -> {
            dismissLoading();
            if (getDownloadUrlQrcodeBean != null && getDownloadUrlQrcodeBean.getData() != null) {
                GetDownloadUrlQrcodeBean data = getDownloadUrlQrcodeBean.getData();
//                二维码图
                ImageLoader.loadImage(binding.ivQrcode, data.getQrcode_image(), 0, 0);
            }
        });

        //邀请好友下载APP--> TODO：分享 TODO：
        binding.tvInviteFriends.setOnClickListener(v -> {
            ShareDialog shareDialog = new ShareDialog();

            shareDialog.show(getFragmentManager(), "ShareDialog");
            shareDialog
                    .setUrl(String.format("%1$sdown",
                            TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl))
//                    .setUrl("http://api.inchengzi.com/down")
                    .setTitle(getString(R.string.app_name))
                    .setContent(AccountHelper.getNickname() + "邀请你下载" + getString(R.string.app_name))
                    .setShareImage(null);
        });
    }
}