package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.OnlineQuestionAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityResolvingQuestionBinding;
import com.chengzi.app.event.AnswerOnlineQuestionEvent;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.ResolvingQuestionViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ResolvingQuestionActivity extends BaseActivity<ActivityResolvingQuestionBinding,
        ResolvingQuestionViewModel> {

    private OnlineQuestionAdapter adapter;

    public static void start(Context context, int status) {
        Intent intent = new Intent(context, ResolvingQuestionActivity.class);
        intent.putExtra(Sys.EXTRA_QUESTION_STATUS, status);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_resolving_question;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        int status = getIntent().getIntExtra(Sys.EXTRA_QUESTION_STATUS, Sys.EXTRA_STATUS_RESOLVED);
        if (status == Sys.EXTRA_STATUS_RESOLVED) {
            mBinding.topBar.setCenterText(getString(R.string.resolved_question));
        } else {
            mBinding.topBar.setCenterText(getString(R.string.resolving_question));
        }
        mViewModel.setStatus(String.valueOf(status));
        initTopBar();

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        adapter = new OnlineQuestionAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String questionId = ((QAbean) adapter.getItem(position)).getId();
            QuestionDetailActivity.start(this, questionId);
        });

        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });
        helper.start();
    }

    private void initTopBar() {
        mBinding.topBar.setOnRightIconClickListener(v -> {
            int status = getIntent().getIntExtra(Sys.EXTRA_QUESTION_STATUS, Sys.EXTRA_STATUS_RESOLVED);
            QuestionSearchActivity.start(this, status);
        });
    }

    @Subscribe
    public void onNewAnswer(AnswerOnlineQuestionEvent event) {
        for (Object object : adapter.getData()) {
            if (object instanceof QAbean) {
                QAbean bean = (QAbean) object;
                if (TextUtils.equals(event.getQuestionId(), bean.getId())) {
                    bean.setAwser(event.getAnswerNum());
                    adapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }

}
