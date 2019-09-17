package com.chengzi.app.ui.mine.activity.vip;

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
import com.chengzi.app.databinding.ActivityLookVipIntroduceBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;
import com.chengzi.app.utils.JavaMyWebViewClient;

/**
 * ① 查看VIP特权介绍 4种身份通用
 * ② 首页/VIP排行榜的查看规则
 *
 * @ClassName:LookVipIntroduceActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/2 0002   14:51
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class LookVipIntroduceActivity extends BaseActivity<ActivityLookVipIntroduceBindingImpl, MyVipViewModel> {

    /**
     * 文本id [1 关于我们 2 隐私政策 3 用户协议 4 联系我们 5 意见反馈] 6 排行榜规则 [7 帮助中心] 8 用户vip规则介绍
     * 9 咨询师vip规则介绍 10医生vip规则介绍 11医院vip规则介绍 12推广规则 13美人筹规则
     * 14-用户使用协议  15-医生使用协议  16-咨询师使用协议  17-医院使用协议
     *
     * @param context
     * @param t_id    6-排行榜规则
     */
    public static void start(Context context, String t_id) {
        context.startActivity(new Intent(context, LookVipIntroduceActivity.class)
                .putExtra("t_id", t_id)
        );
    }

    private String t_id;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_look_vip_introduce;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
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

        showLoading(Sys.LOADING);
        t_id = getIntent().getStringExtra("t_id");
        if (!TextUtils.isEmpty(t_id)) { //①首页/VIP排行榜的查看规则
            mViewModel.getTexts(t_id);
        } else {                    //四种身份的VIP介绍
            dismissLoading();
//            mViewModel.vipAgreeenment();
        }
        mViewModel.vipAgreeenmentLiveData.observe(this, agressmentBean -> {
            dismissLoading();
            if (agressmentBean != null) {
                //标题 内容
                mBinding.topBar.setCenterText(agressmentBean.getTitle());
                mBinding.webView.loadData(agressmentBean.getContent(), "text/html; charset=utf-8", "UTF-8");
            }
        });
    }
}