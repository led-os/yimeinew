package com.chengzi.app.ui.message.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.ForumAdapter;
import com.chengzi.app.databinding.ActivityAtMeBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.event.ForumCollectEvent;
import com.chengzi.app.event.ForumThumbUpEvent;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.message.viewmodel.AtMeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @我 消息
 * @ClassName:AtMeActivity

 * @PackageName:com.yimei.app.ui.message.activity

 * @Create On 2019/4/11 0011   17:10

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class AtMeActivity extends BaseActivity<ActivityAtMeBinding, AtMeViewModel> {
    private ForumAdapter adapter;
    private PagingLoadHelper helper;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_at_me;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        initRecyclerView();
        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        observe();
        mViewModel.setLifecycleOwner(this);
        helper.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.userInfo();
    }

    private void initRecyclerView() {
        adapter = new ForumAdapter();
        adapter.setShowBtn(false);
        adapter.setViewModel(mViewModel);
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void observe() {
        mViewModel.forumListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        mViewModel.forumDetailLive.observe(this, forumDetail -> {
            ForumBean bean = null;
            for (ForumBean forumBean : adapter.getData()) {
                if (TextUtils.equals(forumBean.getId(), forumDetail.getId())) {
                    forumBean.setComment_count(forumDetail.getComment_count());
                    mViewModel.commentObservable.update();
                    break;
                }
            }
        });

    }

    @Subscribe
    public void onForumCollect(ForumCollectEvent event) {
        if (event.getViewModel() == mViewModel) {
            return;
        }

        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isCollect() ? 1 : 0;
                forumBean.setIs_collect(integer);
                String collection_num = forumBean.getCollection_num();
                forumBean.setCollection_num(String.valueOf(Integer.valueOf(collection_num) + (integer == 0 ? -1 : 1)));
                mViewModel.collectObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onForumThumbUp(ForumThumbUpEvent event) {
        if (event.getViewModel() == mViewModel) {
            return;
        }

        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getId(), event.getForumId())) {
                int integer = event.isThumbUp() ? 1 : 0;
                forumBean.setIs_like(integer);
                String likes_num = forumBean.getLikes_num();
                forumBean.setLikes_num(String.valueOf(Integer.valueOf(likes_num) + (integer == 0 ? -1 : 1)));
                mViewModel.thumbUpObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onForumAttention(AttentionEvent event) {

        for (ForumBean forumBean : adapter.getData()) {
            if (TextUtils.equals(forumBean.getUser_id(), event.getUserId())) {
                forumBean.setIs_follow(event.isAttention() ? 1 : 0);
                mViewModel.attentionObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 3) {
            return;
        }
        mViewModel.getForumDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.getForumDetail(event.getTarget());
    }


    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.getForumDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.getForumDetail(event.getTarget());

    }

}
