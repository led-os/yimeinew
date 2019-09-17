package com.chengzi.app.ui.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPeopleSayDetailBinding;
import com.chengzi.app.databinding.ItemPeopleSayDetailHeaderLayoutBinding;
import com.chengzi.app.databinding.ItemPeopleSayDetailLayoutBinding;
import com.chengzi.app.event.PeopleSayAnswerEvent;
import com.chengzi.app.event.PeopleSayAskEvent;
import com.chengzi.app.ui.goods.bean.PeopleSayAnswerBean;
import com.chengzi.app.ui.goods.viewmodel.PeopleSayDetailViewModel;

import org.greenrobot.eventbus.EventBus;

public class PeopleSayDetailActivity extends BaseActivity<ActivityPeopleSayDetailBinding,
        PeopleSayDetailViewModel> implements View.OnClickListener {

    private ItemPeopleSayDetailHeaderLayoutBinding headerBinding;

    public static void start(Context context, String questionId, String targetId, int targetType) {
        Intent intent = new Intent(context, PeopleSayDetailActivity.class);
        intent.putExtra(Sys.EXTRA, questionId)
                .putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(Sys.EXTRA_TARGET_TYPE, targetType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_people_say_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setIsSelf(TextUtils.equals(mViewModel.getTargetId(), AccountHelper.getUserId()));
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecycler();

        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });

        mViewModel.answerLive.observe(this, aBoolean -> {
            dismissLoading();
            ToastUtils.showShort("回答成功");
            helper.start();
            EventBus.getDefault().post(new PeopleSayAnswerEvent(mViewModel.getTargetId(), mViewModel.getTargetType()));
            EventBus.getDefault().post(new PeopleSayAskEvent(mViewModel.getTargetId(), mViewModel.getTargetType()));
        });

        mViewModel.askOthersDetailLive.observe(this, peopleSayQuestionBean -> {
            headerBinding.setBean(peopleSayQuestionBean);
        });

        helper.start();
        mViewModel.getQuestionDetail();
    }

    private void parseIntent() {

        Intent intent = getIntent();

        String questionId = intent.getStringExtra(Sys.EXTRA);
        mViewModel.setQuestionId(questionId);

        String targetId = intent.getStringExtra(Sys.EXTRA_TARGET_ID);
        int targetType = intent.getIntExtra(Sys.EXTRA_TARGET_TYPE, 1);

        mViewModel.setTargetId(targetId);
        mViewModel.setTargetType(targetType);

    }

    private void initRecycler() {
        PeopleSayDetailAdapter adapter = new PeopleSayDetailAdapter();
        adapter.setHeaderAndEmpty(true);
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        View header = createHeader();
        adapter.addHeaderView(header);
    }

    private View createHeader() {
        headerBinding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.item_people_say_detail_header_layout,
                        mBinding.swipeRefreshView.getRecyclerView(), false);
        return headerBinding.getRoot();
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
                if (TextUtils.isEmpty(mViewModel.answerContent.get())) {
                    ToastUtils.showShort("请输入回答的内容");
                    return;
                }
                showLoading("");
                mViewModel.answerOthers();
                break;
        }

    }

    private static class PeopleSayDetailAdapter extends BaseQuickAdapter<PeopleSayAnswerBean, BaseViewHolder> {

        public PeopleSayDetailAdapter() {
            super(R.layout.item_people_say_detail_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, PeopleSayAnswerBean item) {
            ItemPeopleSayDetailLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }
}
