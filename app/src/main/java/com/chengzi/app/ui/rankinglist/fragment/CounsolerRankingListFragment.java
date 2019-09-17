package com.chengzi.app.ui.rankinglist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.DoctorListAdapter;
import com.chengzi.app.databinding.FragmentCounsolerRankingListBinding;
import com.chengzi.app.ui.rankinglist.viewmodel.CounselorRankingListViewModel;

public class CounsolerRankingListFragment extends RankingListBaseFragment
        <FragmentCounsolerRankingListBinding, CounselorRankingListViewModel> {

    private PagingLoadHelper helper;

    public CounsolerRankingListFragment() {
    }

    public static CounsolerRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        CounsolerRankingListFragment fragment = new CounsolerRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_counsoler_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        DoctorListAdapter adapter = new DoctorListAdapter(3);
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPullupEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);

        viewModel.doctorsListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });
    }

    @Override
    protected void loadData() {
        helper.start();
    }
}
