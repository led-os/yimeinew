package com.chengzi.app.ui.homepage.hospital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.CaseListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSubCaseBinding;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalCaseListActivity;
import com.chengzi.app.ui.homepage.hospital.viewmodel.SubCaseViewModel;
import com.chengzi.app.ui.mine.activity.popularize.CaseSearchPromotionActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 医院主页 -- 案例展示(官方案例展示)
 *
 * @ClassName:SubCaseFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   13:32
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class SubCaseFragment extends RefreshableFragment<FragmentSubCaseBinding, SubCaseViewModel>
        implements View.OnClickListener {

    private CaseListAdapter adapter;

    public SubCaseFragment() {
    }

    public static SubCaseFragment newInstance(FragmentManager fm, String hospitalId, boolean isSelf) {
        Fragment fragment = fm.findFragmentByTag(SubCaseFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new SubCaseFragment();
        }
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, hospitalId);
        args.putBoolean("isSelf", isSelf);
        fragment.setArguments(args);
        return (SubCaseFragment) fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sub_case;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        viewModel.setLifecycleOwner(this);

        String hospitalId = getArguments().getString(Sys.EXTRA);
        viewModel.setHospitalId(hospitalId);
        viewModel.setSelf(getArguments().getBoolean("isSelf", false));
        binding.setListener(this);

        adapter = new CaseListAdapter();
        adapter.setIsShowHeadLayout(false);
        adapter.setBaseViewModel(viewModel);
        binding.recyclerCase.setAdapter(adapter);
        binding.recyclerCase.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        viewModel.casesLive.observe(this, caseBeanPageBean -> {
            refreshComplete = true;
            finishRefresh();
            adapter.setNewData(caseBeanPageBean.getData());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.getHospitalCases();
    }

    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_all_case:    //查看 全部案例列表
                //自己的医院则跳转到案例管理 否则是别人的医院查看全部案例列表
                if (viewModel.isSelf()) {
                    startActivity(new Intent(getContext(), CaseSearchPromotionActivity.class)
                            .putExtra("form", 1)
                    );
                } else {
                    HospitalCaseListActivity.start(getContext(), viewModel.getHospitalId());
                }
                break;
        }
    }

    @Subscribe
    public void onCaseThumbUp(CaseThumbUpEvent event) {
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getCaseId())) {
                caseBean.setIs_relation(event.isThumbUp() ? 1 : 0);
                caseBean.setLike(event.isThumbUp());
                caseBean.setLike_number(caseBean.getLike_number() + (event.isThumbUp() ? 1 : -1));
                viewModel.thumbUpObservable.update();
                break;
            }
        }
    }
}
