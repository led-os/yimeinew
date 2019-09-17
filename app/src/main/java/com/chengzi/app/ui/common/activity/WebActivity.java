package com.chengzi.app.ui.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivityWebBinding;


public class WebActivity extends BaseActivity<HandActivityWebBinding,BaseViewModel> {

    public static final String LINK_URL = "link_url";

    public static void start(Context context,String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(LINK_URL,url);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.hand_activity_web;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        String linkUrl = getIntent().getStringExtra(LINK_URL);
        if (TextUtils.isEmpty(linkUrl)) {
            onBackPressed();
            return;
        }

        if (!linkUrl.startsWith("http://") && !linkUrl.startsWith("https://")) {
            linkUrl = "https://" + linkUrl;
        }

        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                mBinding.webView.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mBinding.webView.loadUrl(url);
                return true;
            }
        });
        mBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mBinding.topBar.setCenterText(title);
            }
        });
        mBinding.webView.loadUrl(linkUrl);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.webView != null && mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding.webView != null) {
            mBinding.webView.destroy();
        }
    }

}
