package com.chengzi.app.ui.homepage.doctor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.MainDoctorAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentRecommendOthersBinding;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.doctor.viewmodel.RecommandOthersViewModel;

/**
 * 主页 推荐的  医生/咨询师
 *
 * @ClassName:RecommandOthersFragment
 * @PackageName:com.yimei.app.ui.doctorhomepage.fragment
 * @Create On 2019/4/18 0018   11:17
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class RecommendOthersFragment extends RefreshableFragment<FragmentRecommendOthersBinding,
        RecommandOthersViewModel>
        implements OnRefreshListener {

    public RecommendOthersFragment() {
    }

    public static RecommendOthersFragment newInstance(FragmentManager fm, int userType) {
        RecommendOthersFragment fragment = (RecommendOthersFragment) fm.findFragmentByTag(RecommendOthersFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new RecommendOthersFragment();
        }
        android.os.Bundle args = new Bundle();
        args.putInt(Sys.EXTRA, userType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_recommend_others;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        int userType = getArguments().getInt(Sys.EXTRA, 2);
        viewModel.setUserType(userType);

        binding.tvRecommendTitle.setText(viewModel.getUserType() == 2 ?
                getString(R.string.recommend_doctor) : getString(R.string.recommend_counselor));

        MainDoctorAdapter adapter = new MainDoctorAdapter(userType);
        binding.recyclerRecommand.setAdapter(adapter);

        viewModel.recommendOthersLive.observe(this, doctorBeans -> {
            adapter.setNewData(doctorBeans);
            binding.getRoot().setVisibility(adapter.getData().isEmpty() ? View.GONE : View.VISIBLE);
            refreshComplete = true;
            finishRefresh();
        });

    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.recommendUsers();
    }

    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }
}
