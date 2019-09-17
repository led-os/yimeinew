package com.chengzi.app.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHelpDetailsBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.HelpViewModel;
import com.chengzi.app.utils.JavaMyWebViewClient;

/**
 * 帮助反馈详情
 *
 * @ClassName:HelpDetailsActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/5/8 0008   16:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/8 0008 handongkeji All rights reserved.
 */
public class HelpDetailsActivity extends BaseActivity<ActivityHelpDetailsBindingImpl, HelpViewModel> {

    //标题 详情webView
    private String title;
    private String content;

    public static void start(Context context, String title, String content) {
        context.startActivity(new Intent(context, HelpDetailsActivity.class)
                .putExtra("title", title)
                .putExtra("content", content)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_help_details;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        mBinding.tvTitle.setText(title);

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
//        mBinding.webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//        mBinding.webView.loadUrl("https://new.qq.com/omn/20190402/20190402A05V62.html");

        mBinding.webView.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }
}
