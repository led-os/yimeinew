package com.chengzi.app.ui.comment.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.widget.KeyboardLayout1;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CommentAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCommentDetailBinding;
import com.chengzi.app.databinding.CommentDetailHeaderLayoutBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.viewmodel.CommentDetailViewModel;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 评论详情
 *
 * @ClassName:CommentDetailActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/3/30 0030   16:23
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/3/30 0030 handongkeji All rights reserved.
 */
public class CommentDetailActivity extends BaseActivity<ActivityCommentDetailBinding,
        CommentDetailViewModel> implements View.OnClickListener {

    private CommentDetailHeaderLayoutBinding headerLayoutBinding;
    private CommentAdapter adapter;

    /**
     * @param commentId   父评论id
     * @param commentType 评论类型
     */
    public static void start(Context context, String targetId, String commentId, int commentType) {
        Intent intent = new Intent(context, CommentDetailActivity.class)
                .putExtra(Sys.EXTRA_TARGET_ID, targetId)
                .putExtra(Sys.EXTRA_COMMENT_ID, commentId)
                .putExtra(Sys.EXTRA_COMMENT_TYPE, commentType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_comment_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setViewModel(mViewModel);
        mBinding.setListener(this);

        mViewModel.isComment.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mViewModel.isComment.get()) {
                    mBinding.editComment.setFocusable(true);
                    mBinding.editComment.setFocusableInTouchMode(true);
                    mBinding.editComment.requestFocus();
                    KeyboardUtils.showSoftInput(CommentDetailActivity.this);
                }
            }
        });

        initRecyclerView();
        setKeyboardListener();
        observe();

        mViewModel.fetchCommentDetail();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        String commentId = intent.getStringExtra(Sys.EXTRA_COMMENT_ID);
        int commentType = intent.getIntExtra(Sys.EXTRA_COMMENT_TYPE, 1);
        String targetId = intent.getStringExtra(Sys.EXTRA_TARGET_ID);
        mViewModel.setCommentId(commentId);
        mViewModel.setCommentType(commentType);
        mViewModel.setTargetId(targetId);
    }

    private void observe() {
        mViewModel.commentDetailLive.observe(this, bean -> {

            mBinding.swipeRefreshView.setRefreshing(false);

            List<CommentBean> child_comment = bean.getChild_comment();
            adapter.setNewData(child_comment);
            headerLayoutBinding.getRoot().setVisibility(View.VISIBLE);
            headerLayoutBinding.setBean(bean);
            headerLayoutBinding.setIsSelef(TextUtils.equals(AccountHelper.getUserId(), bean.getFrom_uid()));
            mViewModel.setCommentBean(bean);
            if (mViewModel.isNewChildComment()) {
                mViewModel.setNewChildComment(false);
                mBinding.swipeRefreshView.getRecyclerView()
                        .scrollToPosition(adapter.getItemCount() - 1);
            }
        });

        mViewModel.addCommentLive.observe(this, commentId -> {
            ToastUtils.showShort("评论成功");
            mViewModel.setNewChildComment(true);
            mViewModel.fetchCommentDetail();
            mBinding.swipeRefreshView.getRecyclerView()
                    .scrollToPosition(adapter.getItemCount() - 1);
            EventBus.getDefault().post(new AddChildCommentEvent(mViewModel.getTargetId(), mViewModel.getCommentId(), mViewModel.getCommentType()));
        });

        mViewModel.deleteLive.observe(this, commentId -> {

            ToastUtils.showShort("删除评论成功");

            CommentBean bean = mViewModel.commentDetailLive.getValue();
            if (bean == null) {
                return;
            }
            if (TextUtils.equals(commentId, bean.getId())) {
                EventBus.getDefault().post(new DelCommentEvent(mViewModel.getTargetId(), mViewModel.getCommentType()));
                finish();
            } else {
                for (CommentBean commentBean : adapter.getData()) {
                    if (TextUtils.equals(commentBean.getId(), commentId)) {
                        EventBus.getDefault().post(new DelChildCommentEvent(mViewModel.getTargetId(), bean.getId(), mViewModel.getCommentType()));
                        break;
                    }
                }
                mViewModel.fetchCommentDetail();
            }

        });
    }

    private void initRecyclerView() {
        adapter = new CommentAdapter(mViewModel.getCommentType());
        adapter.addHeaderView(createHeader());
        adapter.setHeaderAndEmpty(true);
        adapter.setHasStableIds(true);

        mBinding.swipeRefreshView.setOnRefreshListener(() -> {
            mViewModel.fetchCommentDetail();
        });

        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.setPullupEnable(false);

        adapter.setListener(new OnCommentClickListener() {
            @Override
            public void onContent(View view, CommentBean bean) {
                mViewModel.isComment.set(true);
                if (TextUtils.isEmpty(bean.getPid())) {
                    bean.setPid(mViewModel.getCommentId());
                }
                mViewModel.grandComment(bean);
            }

            @Override
            public void onDelete(View view, CommentBean bean) {
                mViewModel.deleteComment(bean.getId());
            }
        });
    }

    private View createHeader() {
        headerLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.comment_detail_header_layout, mBinding.swipeRefreshView.getRecyclerView(), false);
        headerLayoutBinding.getRoot().setVisibility(View.INVISIBLE);
        headerLayoutBinding.setListener(new ClickEventHandler<CommentBean>() {
            @Override
            public void handleClick(View view, CommentBean bean) {
                if (!ClickEvent.shouldClick(view)) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.image_head:
                    case R.id.tv_name:

                        int user_type = bean.getUser_type();
                        if (user_type == 1) {
                            UserHomePageActivity.start(view.getContext(), bean.getFrom_uid());
                        } else if (user_type == 2) {
                            DoctorHomePageActivity.startDoctor(view.getContext(), bean.getFrom_uid());
                        } else if (user_type == 3) {
                            DoctorHomePageActivity.startCounselor(view.getContext(), bean.getFrom_uid());
                        } else {
                            HospitalHomePageActivity.start(view.getContext(), bean.getFrom_uid());
                        }
                        break;
                    case R.id.tv_comment_content:

                        break;
                    case R.id.btn_delete:
                        new QMUIDialog.MessageDialogBuilder(view.getContext())
                                .setMessage("确定删除该条评论?")
                                .addAction(0, R.string.cancel, QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                                    dialog.dismiss();
                                })
                                .addAction(0, R.string.confirm, QMUIDialogAction.ACTION_PROP_POSITIVE, (dialog, index) -> {
                                    mViewModel.deleteComment(bean.getId());
                                    dialog.dismiss();
                                })
                                .show();

                        break;
                }
            }
        });
        return headerLayoutBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_comment:
                mViewModel.isComment.set(true);
                mViewModel.childComment(mViewModel.getCommentBean());
                break;
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
