package com.chengzi.app.ui.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PeopleSayListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPeopleSaysBinding;
import com.chengzi.app.event.PeopleSayAnswerEvent;
import com.chengzi.app.event.PeopleSayAskEvent;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;
import com.chengzi.app.ui.goods.viewmodel.PeopleSaysViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 大家说
 *
 * @ClassName:PeopleSaysActivity
 * @PackageName:com.yimei.app.ui.goods.activity
 * @Create On 2019/4/17 0017   10:04
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/17 0017 handongkeji All rights reserved.
 */
public class PeopleSaysActivity extends BaseActivity<ActivityPeopleSaysBinding,
        PeopleSaysViewModel> implements OnClickListener {

    public static final String EXTRA_QUESTION_ABOUT = "extra_question_about";
    private String questionAbout;
    private TextView textView;
    private PagingLoadHelper helper;

    public static void start(Context context, String targetId, int targetType, String questionAbout) {
        Intent intent = new Intent(context, PeopleSaysActivity.class);
        intent.putExtra(Sys.EXTRA_TARGET_TYPE, targetType)
                .putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(EXTRA_QUESTION_ABOUT, questionAbout);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_people_says;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        parseIntent();

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
        mBinding.setIsSelf(TextUtils.equals(mViewModel.getTargetId(), AccountHelper.getUserId()));

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecycler();

        mViewModel.peopleSayQuestionLive.observe(this, peopleSayQuestionBeanPageBean -> {

            List<PeopleSayQuestionBean> beans = peopleSayQuestionBeanPageBean.getData();
            helper.onComplete(beans);
            if (textView != null) {
                textView.setText(getString(R.string.question_hint, questionAbout, beans.size()));
            }

        });

        mViewModel.publishAskLive.observe(this, aBoolean -> {

            dismissLoading();
            ToastUtils.showShort("提问成功");
            helper.start();
            EventBus.getDefault().post(new PeopleSayAskEvent(mViewModel.getTargetId(), mViewModel.getTargetType()));
        });

        helper.start();

    }

    private void parseIntent() {
        Intent intent = getIntent();
        String targetId = intent.getStringExtra(Sys.EXTRA_TARGET_ID);
        int targetType = intent.getIntExtra(Sys.EXTRA_TARGET_TYPE, 1);
        questionAbout = intent.getStringExtra(EXTRA_QUESTION_ABOUT);

        mViewModel.setTargetId(targetId);
        mViewModel.setTargetType(targetType);
    }

    private void initRecycler() {
        PeopleSayListAdapter adapter = new PeopleSayListAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        View header = createHeader();
        adapter.addHeaderView(header);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            PeopleSayQuestionBean item = adapter.getItem(position);
            PeopleSayDetailActivity.start(this, item.getId(), mViewModel.getTargetId(), mViewModel.getTargetType());
        });
    }

    private View createHeader() {
        textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = params.bottomMargin = params.rightMargin = SizeUtils.dp2px(10);
        params.topMargin = SizeUtils.dp2px(15);
        textView.setLayoutParams(params);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.color_3));
        return textView;
    }

    @Override
    public void onClick(View v) {

        if (!ClickEvent.shouldClick(v)) {
            return;
        }

        if (AccountHelper.shouldLogin(this)) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(mViewModel.questionContent.get())) {
                    ToastUtils.showShort("请输入您的问题");
                    return;
                }
                showLoading("");
                mViewModel.submitQuestion();
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    @Subscribe
    public void updatePeopleSayAnswer(PeopleSayAnswerEvent event) {
        if (TextUtils.equals(event.getTargetId(), mViewModel.getTargetId()) &&
                event.getTargetType() == mViewModel.getTargetType()) {
            helper.onPulldown();
        }
    }

}
