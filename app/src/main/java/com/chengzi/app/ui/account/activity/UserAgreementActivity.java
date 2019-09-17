package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityUserAgreementBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.UserAgreementViewModel;
import com.chengzi.app.utils.JavaMyWebViewClient;

/**
 * 用户协议
 *
 * @ClassName:UserAgreementActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   15:16
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */
public class UserAgreementActivity extends BaseActivity<ActivityUserAgreementBindingImpl, UserAgreementViewModel> {
    //用户身份 1普通用户 2医生 3咨询师 4医院
    private int type;

    //1推广规则 默认是用户协议
    private int form;

    public static void strat(Context context, int form) {
        context.startActivity(new Intent(context, UserAgreementActivity.class)
                .putExtra("form", form));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_agreement;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
//        mBinding.setListener(this);
        form = getIntent().getIntExtra("form", 0);
        showLoading(Sys.LOADING);
        if (form == 1) {
            mBinding.topBar.setCenterText("推广规则");
            mViewModel.promotionAgreement();
        } else if (form == 0) {
            mViewModel.getAgreementDetails();
        }

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //weView中有链接,在当前 browser 中相应
        mBinding.webView.setWebViewClient(new JavaMyWebViewClient());
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.getSettings().setDefaultTextEncodingName("utf-8");
//        mBinding.webView.loadDataWithBaseURL(null, "哈哈哈哈哈哈哈"
//                , "text/html; charset=utf-8", "UTF-8", null);
        //推广规则
        mViewModel.promotionAgreementLiveData.observe(this, promotionAgreementBean -> {
            dismissLoading();
            if (promotionAgreementBean != null && promotionAgreementBean.getData() != null) {
                String content = promotionAgreementBean.getData().getContent();
                if (!TextUtils.isEmpty(content)) {
                    mBinding.webView.loadDataWithBaseURL(null, content, "text/html; charset=utf-8", "UTF-8", null);
                }
            }
        });
        //用户使用协议
        mViewModel.getAgreementDetailsLiveData.observe(this, promotionAgreementBean -> {
            dismissLoading();
            if (promotionAgreementBean != null && promotionAgreementBean.getData() != null) {
                //TopBarText 和 webView
                mBinding.topBar.setCenterText(promotionAgreementBean.getData().getTitle());
                String content = promotionAgreementBean.getData().getContent();
                if (!TextUtils.isEmpty(content)) {
                    mBinding.webView.loadDataWithBaseURL(null, content, "text/html; charset=utf-8", "UTF-8", null);
                }
            }
        });
    }
}