package com.chengzi.app.ui.rankinglist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.DoctorListAdapter;
import com.chengzi.app.databinding.FragmentDoctorRankingListBinding;
import com.chengzi.app.ui.rankinglist.viewmodel.DoctorRankingListViewModel;

/**
 * 医生排行榜
 *
 * @ClassName:DoctorRankingListFragment
 * @PackageName:com.yimei.app.ui.rankinglist.fragment
 * @Create On 2019/4/10 0010   13:32
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class DoctorRankingListFragment extends RankingListBaseFragment<FragmentDoctorRankingListBinding,
        DoctorRankingListViewModel> {

    private PagingLoadHelper helper;

    public DoctorRankingListFragment() {
    }

    public static DoctorRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        DoctorRankingListFragment fragment = new DoctorRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_doctor_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        DoctorListAdapter adapter = new DoctorListAdapter(2);
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
