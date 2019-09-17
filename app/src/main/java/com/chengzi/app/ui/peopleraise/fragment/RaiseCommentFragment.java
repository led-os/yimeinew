package com.chengzi.app.ui.peopleraise.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CommentAdapter;
import com.chengzi.app.databinding.FragmentRaiseCommentBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.activity.AllCommentsActivity;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.peopleraise.viewmodel.PeopleRaiseDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 美人筹详情--评论
 *
 * @ClassName:RaiseCommentFragment
 * @PackageName:com.yimei.app.ui.peopleraise.fragment
 * @Create On 2019/5/13 0013   14:15
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/13 0013 handongkeji All rights reserved.
 */
public class RaiseCommentFragment extends BaseFragment<FragmentRaiseCommentBinding,
        PeopleRaiseDetailViewModel> implements View.OnClickListener {

    private CommentAdapter commentAdapter;

    public RaiseCommentFragment() {
    }

    public static RaiseCommentFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(RaiseCommentFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new RaiseCommentFragment();
            android.os.Bundle args = new Bundle();
            fragment.setArguments(args);
        }
        return (RaiseCommentFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_raise_comment;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        binding.setListener(this);
        viewModel.setCommentType(2);
        viewModel.setTargetId(viewModel.getPlanId());
        initCommentRecycler();

        observe();

        viewModel.planCommentList();
    }

    private void observe() {
        viewModel.commentListLive.observe(this, commentBeans -> {
            List<CommentBean> data = commentBeans.getData();
            commentAdapter.setNewData(data);
            int total = commentBeans.getTotal();
            binding.tvCommentNum.setText(getString(R.string.comment_nums, total));
            if (commentAdapter.getData().isEmpty()) {
                commentAdapter.setEmptyView(R.layout.empty_comment_layout);
            }
        });

        viewModel.deleteLive.observe(this, aBoolean -> {
            ToastUtils.showShort("删除评论成功");
            viewModel.planCommentList();
        });

        viewModel.addCommentLive.observe(this, aBoolean -> {
            viewModel.planCommentList();
        });

        viewModel.commentDetailLive.observe(this, bean -> {
            for (CommentBean commentBean : commentAdapter.getData()) {
                if (TextUtils.equals(bean.getId(), commentBean.getId())) {
                    commentBean.setChild_comment(bean.getChild_comment());
                    break;
                }
            }
        });
    }

    private void initCommentRecycler() {

        binding.recyclerComment.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        commentAdapter = new CommentAdapter(2);
        commentAdapter.setTargetId(viewModel.getPlanId());
        commentAdapter.bindToRecyclerView(binding.recyclerComment);
        commentAdapter.setListener(new OnCommentClickListener() {
            @Override
            public void onContent(View view, CommentBean bean) {
                AllCommentsActivity.start(getActivity(), viewModel.getPlanId());
            }

            @Override
            public void onDelete(View view, CommentBean bean) {
                viewModel.deleteComment(bean.getId());
            }
        });
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 2) {
            return;
        }
        for (CommentBean commentBean : commentAdapter.getData()) {
            if (TextUtils.equals(commentBean.getId(), event.getParentId())) {
                viewModel.fetchCommentDetail(commentBean.getId());
                break;
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 2) {
            return;
        }

        viewModel.planCommentList();

    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event){
        if (event.getCommentType() != 2) {
            return;
        }
        viewModel.planCommentList();
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event){
        if (event.getCommentType() != 2) {
            return;
        }
        for (CommentBean commentBean : commentAdapter.getData()) {
            if (TextUtils.equals(commentBean.getId(), event.getParentId())) {
                viewModel.fetchCommentDetail(commentBean.getId());
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_all_comment:  //  查看所有评论
                AllCommentsActivity.start(getActivity(), viewModel.getPlanId());
                break;
        }
    }
}
