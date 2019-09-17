package com.chengzi.app.ui.comment.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.widget.KeyboardLayout1;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CommentAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCommentContainerBinding;
import com.chengzi.app.databinding.ItemCommentCountHeaderLayoutBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.cases.fragment.CaseDetailBottomLayoutFragment;
import com.chengzi.app.ui.cases.fragment.CaseDetailFragment;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.viewmodel.CommentContainerViewModel;
import com.chengzi.app.ui.discover.fragment.ForumDetailBottomLayoutFragment;
import com.chengzi.app.ui.discover.fragment.ForumDetailFragment;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class CommentContainerActivity extends BaseActivity<ActivityCommentContainerBinding,
        CommentContainerViewModel> implements RefreshListenerRegistry {

    private static final String EXTRA_PROMOTION_ID = "extra_promotion_id";

    public static final int COMMENT_CASE = 0x01;
    public static final int COMMENT_FORUM = 0x03;

    private PagingLoadHelper helper;
    private CommentAdapter adapter;

    /**
     * @param context
     * @param targetId    帖子id，案例id
     * @param commentType 1 案例 2 美人筹 3 发现圈
     */
    public static void start(Context context, String targetId, int commentType) {
        Intent intent = new Intent(context, CommentContainerActivity.class);
        intent.putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(Sys.EXTRA_COMMENT_TYPE, commentType);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param form     1案例管理 / 案例站内搜索推广
     * @param targetId 案例id
     */
    public static void start(Context context, int form, String targetId, int commentType) {
        context.startActivity(new Intent(context, CommentContainerActivity.class)
                .putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(Sys.EXTRA_COMMENT_TYPE, commentType)
                .putExtra("form", form)
        );
    }

    /**
     * @param context
     * @param form     1案例管理 / 案例站内搜索推广
     * @param targetId 案例id
     */
    public static void start(Context context, int form, String targetId, int commentType, String promotionId) {
        context.startActivity(new Intent(context, CommentContainerActivity.class)
                .putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(EXTRA_PROMOTION_ID, promotionId)
                .putExtra(Sys.EXTRA_COMMENT_TYPE, commentType)
                .putExtra("form", form)
        );
    }

    private ItemCommentCountHeaderLayoutBinding countHeaderBinding;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_comment_container;
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

        mBinding.setListener(clickEventHandler);
        mBinding.setViewModel(mViewModel);

        mViewModel.isComment.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mViewModel.isComment.get()) {
                    mBinding.editComment.setFocusable(true);
                    mBinding.editComment.setFocusableInTouchMode(true);
                    mBinding.editComment.requestFocus();
                    KeyboardUtils.showSoftInput(CommentContainerActivity.this);
                }
            }
        });

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel) {
            @Override
            public void onPulldown() {
                super.onPulldown();
                for (OnRefreshListener listener : listeners) {
                    listener.onRefresh();
                }
            }
        };

        initRecyclerView();
        setKeyboardListener();

        observe();

        helper.start();
    }

    private void parseIntent() {

        String targetId = getIntent().getStringExtra(Sys.EXTRA_TARGET_ID);
        int commentType = getIntent().getIntExtra(Sys.EXTRA_COMMENT_TYPE, 1);
        mViewModel.setTargetId(targetId);
        mViewModel.setCommentType(commentType);

        if (commentType == 1) {
            mBinding.topBar.setCenterText(getString(R.string.case_detail));
        } else if (commentType == 2) {

        } else if (commentType == 3) {
            mBinding.topBar.setCenterText(getString(R.string.detail));

        }

    }

    public void setUpTopBar(@DrawableRes int rightIcon, Runnable runnable) {
        mBinding.topBar.setRightIcon(rightIcon);
        mBinding.topBar.setOnRightIconClickListener(v -> {
            runnable.run();
        });
    }

    public void observe() {
        mViewModel.commentListLive.observe(this, commentBeans -> {
            helper.onComplete(commentBeans);

        });

        mViewModel.singleCommentListLive.observe(this, commentBeans -> {
            adapter.addData(commentBeans);
        });

        mViewModel.addCommentLive.observe(this, commentId -> {
            ToastUtils.showShort("评论成功");
            if (TextUtils.isEmpty(commentId)) {
                helper.onPulldown();
                EventBus.getDefault().post(new AddCommentEvent(mViewModel.getTargetId(), mViewModel.getCommentType()));
            } else {
                for (OnRefreshListener listener : listeners) {
                    listener.onRefresh();
                }
                mViewModel.fetchCommentDetail(commentId);
                EventBus.getDefault().post(new AddChildCommentEvent(mViewModel.getTargetId(), commentId, mViewModel.getCommentType()));
            }
        });

        mViewModel.commentDetailLive.observe(this, bean -> {
            for (CommentBean commentBean : adapter.getData()) {
                if (TextUtils.equals(bean.getId(), commentBean.getId())) {
                    commentBean.setChild_comment(bean.getChild_comment());
                    break;
                }
            }
        });

        // 删除评论
        mViewModel.deleteLive.observe(this, commentId -> {

            ToastUtils.showShort("删除评论成功");

            EventBus.getDefault().post(new DelCommentEvent(mViewModel.getTargetId(),commentId, mViewModel.getCommentType()));


        });
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        for (CommentBean commentBean : adapter.getData()) {
            if (TextUtils.equals(commentBean.getId(), event.getParentId())) {
                mViewModel.fetchCommentDetail(commentBean.getId());
                break;
            }
        }
    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event) {

        for (int i = 0; i < adapter.getData().size(); i++) {
            if (TextUtils.equals(adapter.getData().get(i).getId(), event.getCommentId())) {
                if (adapter.getData().size() % helper.getPageSize() == 0) {
                    mViewModel.fetchSingle(adapter.getData().size());
                }
                adapter.remove(i);
                for (OnRefreshListener listener : listeners) {
                    listener.onRefresh();
                }
                break;
            }
        }
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        for (CommentBean commentBean : adapter.getData()) {
            if (TextUtils.equals(commentBean.getId(), event.getParentId())) {
                mViewModel.fetchCommentDetail(commentBean.getId());
                for (OnRefreshListener listener : listeners) {
                    listener.onRefresh();
                }
                break;
            }
        }
    }

    private void initRecyclerView() {
        adapter = new CommentAdapter(mViewModel.getCommentType());
        adapter.setTargetId(mViewModel.getTargetId());
        adapter.setHeaderAndEmpty(true);
        adapter.setListener(new OnCommentClickListener() {
            @Override
            public void onContent(View view, CommentBean bean) {
                mViewModel.isComment.set(true);
                mViewModel.childComment(bean);
            }

            @Override
            public void onDelete(View view, CommentBean bean) {
                mViewModel.deleteComment(bean.getId());
            }
        });
        FrameLayout headerContainer = new FrameLayout(this);
        headerContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        headerContainer.setId(R.id.comment_header_container);
        adapter.addHeaderView(headerContainer);

        countHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.item_comment_count_header_layout, mBinding.swipeRefreshView, false);
        adapter.addHeaderView(countHeaderBinding.getRoot());
        countHeaderBinding.getRoot().setVisibility(View.INVISIBLE);

        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            startActivity(new Intent(this, CommentDetailActivity.class));
        });

        headerContainer.post(() -> {
            initHeader();
        });
    }

    private void initHeader() {

        if (mViewModel.getCommentType() == 3) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.comment_header_container, ForumDetailFragment
                                    .newInstance(getSupportFragmentManager(), mViewModel.getTargetId()),
                            ForumDetailFragment.class.getSimpleName())
                    .commitAllowingStateLoss();

            ForumDetailBottomLayoutFragment forumDetailBottomLayoutFragment = ForumDetailBottomLayoutFragment
                    .newInstance(getSupportFragmentManager());
            if (!forumDetailBottomLayoutFragment.isAdded()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.bottom_container, forumDetailBottomLayoutFragment, ForumDetailBottomLayoutFragment.class.getSimpleName())
                        .commit();
            }

        } else if (mViewModel.getCommentType() == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.comment_header_container, CaseDetailFragment.newInstance(getSupportFragmentManager()),
                            CaseDetailFragment.class.getSimpleName())
                    .commitAllowingStateLoss();

            CaseDetailBottomLayoutFragment caseDetailBottomLayoutFragment = CaseDetailBottomLayoutFragment
                    .newInstance(getSupportFragmentManager());
            if (!caseDetailBottomLayoutFragment.isAdded()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.bottom_container, caseDetailBottomLayoutFragment, CaseDetailBottomLayoutFragment.class.getSimpleName())
                        .commit();
            }
        }

    }

    public void setUpCommentNumber(int commentNumber) {
        countHeaderBinding.getRoot().setVisibility(View.VISIBLE);
        String num = commentNumber < 1000 ? String.valueOf(commentNumber) : getString(R.string.format, commentNumber / 1000f);
        countHeaderBinding.tvCommentNum.setText(getString(R.string.comments_total_num, num));
    }

    private void setKeyboardListener() {
        mBinding.keyboardLayout.setOnkbdStateListener(state -> {
            switch (state) {
                case KeyboardLayout1.KEYBOARD_STATE_INIT:
                    break;
                case KeyboardLayout1.KEYBOARD_STATE_HIDE:
                    mViewModel.isComment.set(false);
                    break;
                case KeyboardLayout1.KEYBOARD_STATE_SHOW:
                    break;
            }
        });
    }

    private ClickEventHandler<Object> clickEventHandler = (view, bean) -> {

        if (!ClickEvent.shouldClick(view)) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_send:   //  发送评论
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.content.get())) {
                    ToastUtils.showShort("评论内容不能为空");
                    return;
                }
                mViewModel.addComment();
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    };

    private Rect rect = new Rect();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            rect.setEmpty();
            mBinding.llComment.getGlobalVisibleRect(rect);
            if (mViewModel.isComment.get() && !rect.contains((int) rawX, (int) rawY)) {
                KeyboardUtils.hideSoftInput(this);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private List<OnRefreshListener> listeners = new ArrayList<>();

    @Override
    public void register(OnRefreshListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unRegister(OnRefreshListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void finishRefresh() {

    }
}
