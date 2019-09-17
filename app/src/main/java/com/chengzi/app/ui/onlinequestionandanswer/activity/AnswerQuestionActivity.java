package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityAnswerQuestionBinding;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.AnswerQuestionViewModel;

/**
 * 我要回答
 *
 * @ClassName:AnswerQuestionActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/15 0015   11:33
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class AnswerQuestionActivity extends BaseActivity<ActivityAnswerQuestionBinding,
        AnswerQuestionViewModel> implements View.OnClickListener {

    public static void start(Context context, String questionId, String questionTitle, String questionContent, String questionTypeName) {
        context.startActivity(new Intent(context, AnswerQuestionActivity.class)
                .putExtra("questionId", questionId)
                .putExtra("questionTitle", questionTitle)
                .putExtra("questionContent", questionContent)
                .putExtra("questionTypeName", questionTypeName)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_answer_question;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        parseIntent();
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mViewModel.liveData.observe(this, aBoolean -> {
            if (aBoolean != null && aBoolean) {
                ToastUtils.showShort("回答成功");
                QuestionDetailActivity.startSignle(this, mViewModel.getQuestionId());
                finish();
            }
        });
    }

    private void parseIntent() {
        String questionId = getIntent().getStringExtra("questionId");
        String questionTypeName = getIntent().getStringExtra("questionTypeName");
        String questionTitle = getIntent().getStringExtra("questionTitle");
        String questionContent = getIntent().getStringExtra("questionContent");
        mViewModel.setQuestionId(questionId);
        mBinding.setQuestionContent(questionContent);
        mBinding.setQuestionTitle(questionTitle);
        mBinding.setQuestionTypeName(questionTypeName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                int identity = AccountHelper.getIdentity();
                if (identity <= 1) {
                    ToastUtils.showShort("只有医生、咨询师、医院才可以回答问题");
                    return;
                }

                if (TextUtils.isEmpty(mViewModel.answer.get())) {
                    ToastUtils.showShort("请填写回答内容");
                    return;
                }
                mViewModel.answerQuestion();
                break;
        }
    }
}
