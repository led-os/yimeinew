package com.chengzi.app.ui.mine.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityFeedBackBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.FeedBackViewModel;

/**
 * 意见反馈
 *
 * @ClassName:FeedBackActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/8 0008   14:16
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class FeedBackActivity extends BaseActivity<ActivityFeedBackBindingImpl, FeedBackViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setViewModel(mViewModel);
        //意见反馈提交成功
        mViewModel.feedbackLiveData.observe(this, beans -> {
            dismissLoading();
            if (beans.getData() != null) {
                toast("提交成功!");
                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_submit:  //确认

                if (AccountHelper.shouldLogin(this)) {
                    return;
                }

                ContentBody mobileBody = new ContentBody("联系电话", mViewModel.mobile.get());
                ContentBody code = new ContentBody("意见和反馈", mViewModel.content.get());
                boolean checkResult = ContentChecker.getCheckMachine()
                        .putChecker(ContentChecker.getChecker(mobileBody)
                                .addCondition(new NotEmptyCondition(this))
                                .addCondition(new PhoneNumCondition()))
                        .putChecker(ContentChecker.getChecker(code)
                                .addCondition(new NotEmptyCondition(this)))
                        .checkAll();
                if (checkResult) {
                    showLoading(Sys.LOADING);
                    mViewModel.feedback();
                }
                break;
        }
    };
}
