package com.chengzi.app.ui.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.OnlineQuestionAdapter;
import com.chengzi.app.databinding.ActivityAnswerBinding;
import com.chengzi.app.ui.homepage.viewmodel.AnswerViewModel;
import com.chengzi.app.ui.onlinequestionandanswer.activity.QuestionDetailActivity;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;

import java.util.ArrayList;

/**
 * 医生/咨询师/医院  的回答  //TODO:主页的回答列表
 *
 * @ClassName:AnswerActivity
 * @PackageName:com.yimei.app.ui.homepage.activity
 * @Create On 2019/4/18 0018   18:13
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class AnswerActivity extends BaseActivity<ActivityAnswerBinding, AnswerViewModel> {

    //被查看者id
    private String click_id;

    public static void start(Context context, String click_id) {
        context.startActivity(new Intent(context, AnswerActivity.class)
                .putExtra("click_id", click_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_answer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        click_id = getIntent().getStringExtra("click_id");
        mViewModel.click_id.set(click_id);

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        OnlineQuestionAdapter adapter = new OnlineQuestionAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            QAbean qAbean = (QAbean) adapter.getData().get(position);
            QuestionDetailActivity.start(this, qAbean.getId());
        });

        mViewModel.userAnswersLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        helper.start();
    }
}