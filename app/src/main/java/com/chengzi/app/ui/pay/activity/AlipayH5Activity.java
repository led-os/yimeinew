package com.chengzi.app.ui.pay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAlipayH5Binding;
import com.chengzi.app.ui.pay.viewmodel.AlipayH5ViewModel;

public class AlipayH5Activity extends BaseActivity<ActivityAlipayH5Binding, AlipayH5ViewModel> {

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AlipayH5Activity.class);
        intent.putExtra(Sys.EXTRA, url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_alipay_h5;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String url = getIntent().getStringExtra(Sys.EXTRA);
        String prepayId = getIntent().getStringExtra("prepayId");
        mViewModel.setPrepaylogId(prepayId);

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.d("aaa", "shouldOverrideUrlLoading: " + url);

                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        startActivity(intent);
                        mViewModel.setStartAlipay(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(AlipayH5Activity.this)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    return true;
                }
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });

        mBinding.webView.setWebChromeClient(new WebChromeClient() {
        });
        //weView中有链接,在当前 browser 中相应
//        mBinding.webView.setWebViewClient(new JavaMyWebViewClient());
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.getSettings().setDefaultTextEncodingName("utf-8");

        mBinding.webView.loadUrl(url);

        observe();
    }

    private void observe() {
        mViewModel.payStatusLive.observe(this, integer -> {
            if (integer != 1) {     //  未支付

                if (System.currentTimeMillis() - mViewModel.getStartTime() >= 10000) {
                    // 支付超时
                    payFinished(0);
                } else {
                    //  继续查询状态
                    mViewModel.getPayStatus();
                }

            } else {      //  支付成功
                payFinished(1);
            }
        });
    }

    private void payFinished(int status) {
        Intent intent = new Intent();
        intent.putExtra("pay_result", status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewModel.isPayStart()) {
            mViewModel.setPayStart(false);
            mViewModel.getPayStatus();
            mViewModel.setStartTime(System.currentTimeMillis());
            mBinding.webView.setVisibility(View.GONE);
            mBinding.tvPaying.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewModel.isStartAlipay()) {
            mViewModel.setStartAlipay(false);
            mViewModel.setPayStart(true);
        }
    }
}
