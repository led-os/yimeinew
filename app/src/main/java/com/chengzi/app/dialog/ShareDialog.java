package com.chengzi.app.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogShareLayoutBinding;
import com.chengzi.app.third.ShareUtil;

public class ShareDialog extends BottomDialog implements View.OnClickListener {

    public static final String SHARE_TYPPE_FORUM = "1";
    public static final String SHARE_TYPPE_CASE = "2";

    private DialogShareLayoutBinding mBinding;
    private ShareUtil shareUtil;

    private String type;     //  1 发现，2 案例
    private String targetId;    //   被转发的id
    private ShareViewModel shareViewModel;

    public void setType(String type) {
        this.type = type;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_share_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setListener(this);

        shareUtil = new ShareUtil(getActivity());

        if (!TextUtils.isEmpty(targetId)) {

            shareViewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
            shareViewModel.setTargetId(targetId);
            shareViewModel.setType(type);

            shareViewModel.shareLive.observe(this, aBoolean -> {

            });
        }

        shareUtil.setOnShareStartListener(() -> {
            if (shareViewModel != null) {
                shareViewModel.shareStatistics();
            }
            dismissAllowingStateLoss();
        });
    }

    private String url;
    private String title;
    private String content;
    private String shareImage;

    public ShareDialog setUrl(String url) {
        this.url = url;
        return this;
    }

    public ShareDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ShareDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public ShareDialog setShareImage(String shareImage) {
        this.shareImage = shareImage;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weixin:
                if (shareUtil.share(ShareUtil.WX)) {
                    shareUtil.shareType(ShareUtil.URL, url, title, content, shareImage);
                }
                break;
            case R.id.btn_circle:
                if (shareUtil.share(ShareUtil.FRIEND)) {
                    shareUtil.shareType(ShareUtil.URL, url, title, content, shareImage);
                }
                break;
            case R.id.btn_sina:
                if (shareUtil.share(ShareUtil.SINA)) {
                    shareUtil.shareType(ShareUtil.URL, url, title, content, shareImage);
                }
                break;
            case R.id.btn_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }
}
