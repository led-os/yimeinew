package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAskQuestionBinding;
import com.chengzi.app.dialog.CategoryDialog;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.AskQuestionViewModel;

/**
 * 我要提问
 *
 * @ClassName:AskQuestionActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/13 0013   16:42
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class AskQuestionActivity extends BaseActivity<ActivityAskQuestionBinding,
        AskQuestionViewModel> implements View.OnClickListener {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_ask_question;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mViewModel.liveData.observe(this, aBoolean -> {
            if (aBoolean != null && aBoolean) {
                ResolvingQuestionActivity.start(this, Sys.EXTRA_STATUS_UNRESOLVED);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_select_question_type:
                CategoryDialog categoryDialog = new CategoryDialog();
                categoryDialog.show(getSupportFragmentManager(), CategoryDialog.class.getSimpleName());
                categoryDialog.setListener(navigatorItem -> {
                    mViewModel.setCategoryId(navigatorItem.getId());
                    mViewModel.categoryName.set(navigatorItem.getName());
                });
                break;
            case R.id.btn_submit:

                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                int identity = AccountHelper.getIdentity();
                if (identity > 1) {
                    ToastUtils.showShort("只有普通用户可以提问题");
                    return;
                }

                if (TextUtils.isEmpty(mViewModel.getCategoryId()) || TextUtils.isEmpty(mViewModel.categoryName.get())) {
                    ToastUtils.showShort("请选择问题类型");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.questionDesc.get())) {
                    ToastUtils.showShort("请填写问题描述");
                    return;
                }
                mViewModel.addQuestions();
                break;
        }
    }
}
