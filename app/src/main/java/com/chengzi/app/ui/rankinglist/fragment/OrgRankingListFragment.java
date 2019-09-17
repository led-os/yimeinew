package com.chengzi.app.ui.rankinglist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.OrgListAdapter;
import com.chengzi.app.databinding.FragmentOrgRankingListBinding;
import com.chengzi.app.ui.rankinglist.viewmodel.OrgRankingListViewModel;

public class OrgRankingListFragment extends RankingListBaseFragment<FragmentOrgRankingListBinding,
        OrgRankingListViewModel> {

    private PagingLoadHelper helper;

    public OrgRankingListFragment() {
    }

    public static OrgRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        OrgRankingListFragment fragment = new OrgRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_org_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        OrgListAdapter adapter = new OrgListAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPullupEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);

        viewModel.orgListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });
    }

    @Override
    protected void loadData() {
        helper.start();
    }
}
