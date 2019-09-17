package com.chengzi.app.ui.comment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handongkeji.widget.KeyboardLayout1;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CommentAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAllCommentsBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.viewmodel.AllCommentsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 全部评论 -- 美人筹
 *
 * @ClassName:AllCommentsActivity
 * @PackageName:com.yimei.app.ui.comment.activity
 * @Create On 2019/4/10 0010   09:11
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class AllCommentsActivity extends BaseActivity<ActivityAllCommentsBinding, AllCommentsViewModel>
        implements View.OnClickListener {

    private PagingLoadHelper helper;
    private CommentAdapter adapter;

    /**
     * @param context
     * @param planId  //  美人筹 id
     */
    public static void start(Context context, String planId) {
        Intent intent = new Intent(context, AllCommentsActivity.class);
        intent.putExtra(Sys.EXTRA, planId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_all_comments;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        String planId = getIntent().getStringExtra(Sys.EXTRA);
        mViewModel.setTargetId(planId);
        mViewModel.setCommentType(2); // 此界面 只有美人筹 这里写死为 2

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecyclerView();
        setKeyboardListener();

        observe();

        helper.start();

    }

    private void observe() {
        mViewModel.commentListLive.observe(this, commentBeans -> {
            helper.onComplete(commentBeans);
        });

        mViewModel.singleCommentLive.observe(this, commentBeans -> {
            adapter.addData(commentBeans);
        });

        mViewModel.addCommentLive.observe(this, commentId -> {
            ToastUtils.showShort("评论成功");
            if (TextUtils.isEmpty(commentId)) {
                helper.onPulldown();
                EventBus.getDefault().post(new AddCommentEvent(mViewModel.getTargetId(), mViewModel.getCommentType()));
            } else {
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

        mViewModel.deleteLive.observe(this, commentId -> {

            ToastUtils.showShort("删除评论成功");
            EventBus.getDefault().post(new DelCommentEvent(mViewModel.getTargetId(), mViewModel.getCommentType()));

        });
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 2) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.fetchCommentDetail(event.getParentId());
//        for (CommentBean commentBean : adapter.getData()) {
//            if (TextUtils.equals(commentBean.getId(), event.getParentId())) {
//                break;
//            }
//        }
    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (TextUtils.equals(adapter.getData().get(i).getId(), event.getCommentId())) {

                if (adapter.getData().size() % helper.getPageSize() == 0) {
                    mViewModel.fetchSingleItem(adapter.getData().size());
                }

                adapter.remove(i);
                break;
            }
        }
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        mViewModel.fetchCommentDetail(event.getParentId());
    }

    private void initRecyclerView() {
        adapter = new CommentAdapter(2);
        adapter.setTargetId(mViewModel.getTargetId());
        adapter.setHasStableIds(true);
        adapter.setListener(new OnCommentClickListener() {
            @Override
            public void onContent(View view, CommentBean bean) {
                mViewModel.childComment(bean);
                mViewModel.isComment.set(true);
                mBinding.editComment.setFocusable(true);
                mBinding.editComment.setFocusableInTouchMode(true);
                mBinding.editComment.requestFocus();
                KeyboardUtils.showSoftInput(AllCommentsActivity.this);
            }

            @Override
            public void onDelete(View view, CommentBean bean) {
                mViewModel.deleteComment(bean.getId());
            }
        });

        mBinding.swipeRefreshView.getRecyclerView().setItemAnimator(null);
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            startActivity(new Intent(this, CommentDetailActivity.class));
        });
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.edit_comment:
                mViewModel.topComment();
                mViewModel.isComment.set(true);
                mBinding.editComment.setFocusable(true);
                mBinding.editComment.setFocusableInTouchMode(true);
                mBinding.editComment.requestFocus();
                KeyboardUtils.showSoftInput(this);
                break;
            case R.id.btn_send:
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

}
