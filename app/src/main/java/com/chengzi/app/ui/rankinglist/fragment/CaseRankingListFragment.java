package com.chengzi.app.ui.rankinglist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CaseListAdapter;
import com.chengzi.app.databinding.FragmentCaseRankingListBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.rankinglist.viewmodel.CaseRankingListViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 案例排行榜
 *
 * @ClassName:CaseRankingListFragment
 * @PackageName:com.yimei.app.ui.rankinglist.fragment
 * @Create On 2019/4/10 0010   14:59
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class CaseRankingListFragment extends RankingListBaseFragment<FragmentCaseRankingListBinding,
        CaseRankingListViewModel> {

    private PagingLoadHelper helper;
    private CaseListAdapter adapter;

    public CaseRankingListFragment() {
    }

    public static CaseRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        CaseRankingListFragment fragment = new CaseRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_case_ranking_list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.setLifecycleOwner(this);
        adapter = new CaseListAdapter();
        adapter.setCategoryName(viewModel.getCategoryName());
        adapter.setBaseViewModel(viewModel);
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPullupEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_10));
        binding.swipeRefreshView.addItemDecoration(dividerItemDecoration);

        viewModel.casesListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });
        observe();
    }

    @Override
    protected void loadData() {
        helper.start();
    }

    private void observe(){

        viewModel.caseDetailLive.observe(this,caseNoteListBeans -> {
            if (caseNoteListBeans == null || caseNoteListBeans.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteListBeans.get(0);
            for (CaseBean caseBean : adapter.getData()) {
                if (TextUtils.equals(bean.getCases_id(),caseBean.getId())) {
                    caseBean.setComment_number(bean.getComment_number()+"");
                    viewModel.commentObservable.update();
                    break;
                }
            }
        });
    }

    @Subscribe
    public void onCaseThumbUp(CaseThumbUpEvent event) {
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getCaseId())) {
                caseBean.setIs_relation(event.isThumbUp() ? 1 : 0);
                caseBean.setLike_number(caseBean.getLike_number() + (event.isThumbUp() ? 1 : -1));
                viewModel.thumbUpObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 1) {
            return;
        }
        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }


    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }

}
