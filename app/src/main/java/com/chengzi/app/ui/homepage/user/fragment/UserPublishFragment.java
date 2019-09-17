package com.chengzi.app.ui.homepage.user.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.ForumAdapter;
import com.chengzi.app.databinding.FragmentUserPublishBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.event.ForumCollectEvent;
import com.chengzi.app.event.ForumDelEvent;
import com.chengzi.app.event.ForumThumbUpEvent;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.homepage.user.viewmodel.UserHomePageViewModel;
import com.chengzi.app.ui.homepage.viewmodel.UserPublishViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 用户发表
 *
 * @ClassName:UserPublishFragment
 * @PackageName:com.yimei.app.ui.homepage.user.fragment
 * @Create On 2019/4/19 0019   16:33
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class UserPublishFragment extends BaseFragment<FragmentUserPublishBinding, UserPublishViewModel> {

    private PagingLoadHelper helper;
    private ForumAdapter adapter;

    public UserPublishFragment() {
    }

    /**
     * @param targetId
     * @param is_attention 是否显示关注 (医生/咨询师/医院 发表的帖子->显示  普通用户->不显示)
     * @return
     */
    public static UserPublishFragment newInstance(String targetId, boolean is_attention) {
        UserPublishFragment fragment = new UserPublishFragment();
        Bundle args = new Bundle();
        args.putString("targetId", targetId);
        args.putBoolean("is_attention", is_attention);
        fragment.setArguments(args);
        return fragment;
    }

    public void onRefresh() {
        helper.start();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user_publish;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        Bundle arguments = getArguments();
        String targetId = arguments.getString("targetId");
        viewModel.setTargetId(targetId);

        //是否显示关注按钮
        boolean is_attention = arguments.getBoolean("is_attention");


        initRecyclerView(is_attention);
        viewModel.setLifecycleOwner(this);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        observe();
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.start();
        viewModel.userInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null && getUserVisibleHint()) {
            viewModel.userInfo();
        }
    }

    private void observe() {

        viewModel.userIndexLiveData.observe(this, objects -> {
            helper.onComplete(objects);
        });

        viewModel.forumDetailLive.observe(this, forumDetail -> {
            for (ForumBean forumBean : adapter.getData()) {
                if (TextUtils.equals(forumBean.getId(), forumDetail.getId())) {
                    forumBean.setComment_count(forumDetail.getComment_count());
                    viewModel.commentObservable.update();
                    break;
                }
            }
        });

        UserHomePageViewModel homePageViewModel = ViewModelProviders.of(getActivity())
                .get(UserHomePageViewModel.class);
        homePageViewModel.refreshLive.observe(this, aBoolean -> {
            if (getView() != null && getUserVisibleHint()) {
                helper.start();
            }
        });
    }

    private void initRecyclerView(boolean is_attention) {
        adapter = new ForumAdapter();
        adapter.setFromPublish(true);
        adapter.setViewModel(viewModel);
//        adapter.setShowBtn(false);
        adapter.setShowBtn(is_attention);
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPulldownEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);
        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Subscribe
    public void onForumCollect(ForumCollectEvent event) {
        if (event.getViewModel() == viewModel) {
            return;
        }
        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isCollect() ? 1 : 0;
                forumBean.setIs_collect(integer);
                String collection_num = forumBean.getCollection_num();
                forumBean.setCollection_num(String.valueOf(Integer.valueOf(collection_num) + (integer == 0 ? -1 : 1)));
                viewModel.collectObservable.update();
            }
        }
    }

    @Subscribe
    public void onForumThumbUp(ForumThumbUpEvent event) {
        if (event.getViewModel() == viewModel) {
            return;
        }
        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isThumbUp() ? 1 : 0;
                forumBean.setIs_like(integer);
                String likes_num = forumBean.getLikes_num();
                forumBean.setLikes_num(String.valueOf(Integer.valueOf(likes_num) + (integer == 0 ? -1 : 1)));
                viewModel.thumbUpObservable.update();
            }
        }
    }

    @Subscribe
    public void onForumAttention(AttentionEvent event) {

        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getUser_id(), event.getUserId())) {
//                forumBean.setIs_follow(event.isAttention() ? 1 : 0);
                forumBean.setIs_follow(1);
                viewModel.attentionObservable.update();
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 3) {
            return;
        }
        viewModel.getForumDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getForumDetail(event.getTarget());
    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getForumDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getForumDetail(event.getTarget());

    }


    @Subscribe
    public void onForumDelete(ForumDelEvent event) {
        helper.onPulldown();
    }
}
