package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.netease.nim.uikit.common.ui.recyclerview.decoration.DividerItemDecoration;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityQuestionDetailBinding;
import com.chengzi.app.databinding.ItemAnswerLayoutBinding;
import com.chengzi.app.databinding.ItemQuestionDetailHeaderLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.AnswerOnlineQuestionEvent;
import com.chengzi.app.event.DeleteOnlineQuestionEvent;
import com.chengzi.app.ui.onlinequestionandanswer.bean.AnswerBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QuestionDetailBean;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.QuestionDetailViewModel;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 问题详情
 *
 * @ClassName:QuestionDetailActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/15 0015   11:36
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class QuestionDetailActivity extends BaseActivity<ActivityQuestionDetailBinding,
        QuestionDetailViewModel> implements View.OnClickListener {

    public static final String EXTRA_FROM_SELF = "from_self";

    private PagingLoadHelper helper;
    private AnswerAdapter adapter;

    public static void start(Context context, String questionId) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(Sys.EXTRA, questionId);
        context.startActivity(intent);
    }

    public static void startSignle(Context context, String questionId) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(Sys.EXTRA, questionId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    // 从普通用户主页，提问列表进入
    public static void startFromSelf(Context context, String questionId) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(Sys.EXTRA, questionId);
        intent.putExtra(EXTRA_FROM_SELF, true);
        context.startActivity(intent);
    }

    private ItemQuestionDetailHeaderLayoutBinding headerBinding;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_question_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        parseIntent();

        if (mViewModel.isFromSelf()) {
            mBinding.topBar.setRightIcon(R.drawable.shanchu);
            mBinding.topBar.setOnRightIconClickListener(v -> {
                new MessageDialogBuilder(this)
                        .setMessage("确定删除？")
                        .setSureListener(view -> {
                            mViewModel.deleteQuestion();
                        })
                        .show();
            });
        }

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecyclerView();

        observe();

        helper.start();

    }

    private void parseIntent() {
        String questionId = getIntent().getStringExtra(Sys.EXTRA);
        mViewModel.setQuestionId(questionId);

        boolean fromSelf = getIntent().getBooleanExtra(EXTRA_FROM_SELF, false);
        mViewModel.setFromSelf(fromSelf);
    }

    private void initRecyclerView() {
        adapter = new AnswerAdapter();
        adapter.setHeaderAndEmpty(true);
        View headerView = initHeader();
        adapter.addHeaderView(headerView);

        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.setPullupEnable(false);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

    private void observe() {
        mViewModel.liveData.observe(this, questionDetailBean -> {
            headerBinding.getRoot().setVisibility(View.VISIBLE);

            headerBinding.setBean(questionDetailBean);

            long create_time = questionDetailBean.getCreate_time() * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            headerBinding.tvTime.setText("提交时间：" + sdf.format(new Date(create_time)));

            String content = questionDetailBean.getContent();
            if (!TextUtils.isEmpty(content)) {
                SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
                ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.color_3));
                spanBuilder.append(getString(R.string.question_supplement), fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spanBuilder.append(" ");
                spanBuilder.append(content);
                headerBinding.tvQuestionSupplement.setText(spanBuilder);
            }

            headerBinding.tvQuestionSupplement.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

            mViewModel.setResolved(questionDetailBean.getStatus() == 1);

            headerBinding.executePendingBindings();

            List<AnswerBean> answer = questionDetailBean.getAnswer();
            helper.onComplete(answer);

            if (mViewModel.isUpdated()) {
                mViewModel.setUpdated(false);
                EventBus.getDefault().post(new AnswerOnlineQuestionEvent(mViewModel.getQuestionId(), questionDetailBean.getNum()));
            }

        });

        mViewModel.acceptAnswerLive.observe(this, answer -> {
            dismissLoading();
            QuestionDetailBean detailBean = headerBinding.getBean();
            if (detailBean != null) {
                detailBean.setStatus(1);
                headerBinding.setBean(detailBean);
                mViewModel.setResolved(detailBean.getStatus() == 1);
                answer.setStatus(1);
                adapter.getData().remove(answer);
                adapter.getData().add(0, answer);
                adapter.notifyDataSetChanged();
            }
        });

        mViewModel.deleteQuestionLive.observe(this, aBoolean -> {
            ToastUtils.showShort("删除成功");
            EventBus.getDefault().post(new DeleteOnlineQuestionEvent(mViewModel.getQuestionId()));
            finish();
        });
    }

    private View initHeader() {
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.item_question_detail_header_layout,
                mBinding.swipeRefreshView.getRecyclerView(), false);
        headerBinding.getRoot().setVisibility(View.GONE);
        headerBinding.setListener(this);
        return headerBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        if (!ClickEvent.shouldClick(v)) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_answer:
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                int identity = AccountHelper.getIdentity();
                if (identity <= 1) {
                    ToastUtils.showShort("只有医生、咨询师、医院才可以回答问题");
                    return;
                }
                QuestionDetailBean bean = headerBinding.getBean();
                AnswerQuestionActivity.start(this, bean.getId(), bean.getTitle(), bean.getContent(), bean.getType_name());
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mViewModel.setUpdated(true);
        helper.start();
    }

    private class AnswerAdapter extends BaseQuickAdapter<AnswerBean, BaseViewHolder>
            implements ClickEventHandler<AnswerBean> {

        public AnswerAdapter() {
            super(R.layout.item_answer_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, AnswerBean item) {
            ItemAnswerLayoutBinding itemBinding = DataBindingUtil.bind(helper.itemView);
            itemBinding.setBean(item);
            long create_time = item.getCreate_time() * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

            itemBinding.tvTime.setText("回答于" + sdf.format(new Date(create_time)));

            itemBinding.setListener(this);

            itemBinding.btnAcceptAnswer.setVisibility(mViewModel.isFromSelf() && !mViewModel.isResolved() ? View.VISIBLE : View.GONE);

            itemBinding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, AnswerBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            switch (view.getId()) {
                /*不跳转到主页*/
                /*case R.id.btn_avatar:
                case R.id.tv_name:
                case R.id.tv_title:
                    String user_id = bean.getUser_id();
                    UserHomePageActivity.start(view.getContext(), user_id);
                    break;*/
                case R.id.btn_accept_answer:
                    showLoading("");
                    mViewModel.acceptAnswer(bean);
                    break;
            }

        }
    }
}
