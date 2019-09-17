package com.chengzi.app.ui.discover.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.ForumAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentForumBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.event.ForumCollectEvent;
import com.chengzi.app.event.ForumThumbUpEvent;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.event.PostForumEvent;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.DiscoverViewModel;
import com.chengzi.app.ui.discover.viewmodel.ForumViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends LazyloadFragment<FragmentForumBinding, ForumViewModel> {

    private PagingLoadHelper helper;
    private ForumAdapter adapter;
    private boolean loaded;     //  true 已经加载过数据，false 没有加载过数据，即没有调用过 onLazyload() 方法

    public static ForumFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Sys.EXTRA, type);
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ForumFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_forum;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        int type = getArguments().getInt(Sys.EXTRA, 1);
        viewModel.setType(String.valueOf(type));

        initRecyclerView();
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        observe();
        viewModel.setLifecycleOwner(this);
    }

    private void observe() {
        viewModel.forumListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        viewModel.forumDetailLive.observe(this, forumDetail -> {
            ForumBean bean = null;
            for (ForumBean forumBean : adapter.getData()) {
                if (TextUtils.equals(forumBean.getId(), forumDetail.getId())) {
                    forumBean.setComment_count(forumDetail.getComment_count());
                    viewModel.commentObservable.update();
                    break;
                }
            }
        });

        DiscoverViewModel discoverViewModel = ViewModelProviders.of(getActivity()).get(DiscoverViewModel.class);
        discoverViewModel.userInfoLiveData.observe(this, userInfoBean -> {
            if (userInfoBean == null) {
                return;
            }
            String auth_status = userInfoBean.getAuth_status();
            viewModel.curUserAuthStatus.set(Integer.valueOf(auth_status));
        });
    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event) {
        if (getView() != null && loaded) {
            helper.onPulldown();
        }
    }

    private void initRecyclerView() {
        adapter = new ForumAdapter();
        adapter.setViewModel(viewModel);
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Subscribe
    public void onForumCollect(ForumCollectEvent event) {
        if (event.getViewModel() == viewModel) {
            return;
        }
        if (!TextUtils.equals(viewModel.getType(), event.getUserType() + "")) {
            return;
        }
        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isCollect() ? 1 : 0;
                forumBean.setIs_collect(integer);
                String collection_num = forumBean.getCollection_num();
                forumBean.setCollection_num(String.valueOf(Integer.valueOf(collection_num) + (integer == 0 ? -1 : 1)));
                viewModel.collectObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onForumThumbUp(ForumThumbUpEvent event) {
        if (event.getViewModel() == viewModel) {
            return;
        }
        if (!TextUtils.equals(viewModel.getType(), event.getUserType() + "")) {
            return;
        }
        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isThumbUp() ? 1 : 0;
                forumBean.setIs_like(integer);
                String likes_num = forumBean.getLikes_num();
                forumBean.setLikes_num(String.valueOf(Integer.valueOf(likes_num) + (integer == 0 ? -1 : 1)));
                viewModel.thumbUpObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onForumAttention(AttentionEvent event) {

        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getUser_id(), event.getUserId())) {
                forumBean.setIs_follow(event.isAttention() ? 1 : 0);
                viewModel.attentionObservable.update();
            }
        }
    }

    @Subscribe
    public void onPostForum(PostForumEvent event) {
        if (TextUtils.equals(viewModel.getType(), event.getUserType() + "")) {
            helper.onPulldown();
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

    @Override
    public void onLazyload() {
        loaded = true;
        helper.start();
    }

}
