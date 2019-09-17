package com.chengzi.app.ui.find.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityThreePartiesDetailBindingImpl;
import com.chengzi.app.ui.find.bean.ArticleManageBean;
import com.chengzi.app.utils.JavaMyWebViewClient;

/**
 * 三方详情(人才招聘 行业会议发布 培训发布)
 *
 * @ClassName:ThreePartiesDetailActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/4/19 0019   14:41
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class ThreePartiesDetailActivity extends BaseActivity<ActivityThreePartiesDetailBindingImpl, BaseViewModel> {

    private ArticleManageBean data;

    public static void start(Context context, ArticleManageBean data) {
        context.startActivity(new Intent(context, ThreePartiesDetailActivity.class)
                .putExtra("data", data)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_three_parties_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        data = (ArticleManageBean) getIntent().getSerializableExtra("data");
        mBinding.setBean(data);
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
        mBinding.webView.loadData(data.getContent(), "text/html; charset=utf-8", "UTF-8");
//        mBinding.webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//        mBinding.webView.loadUrl("https://www.baidu.com/");
    }
}
