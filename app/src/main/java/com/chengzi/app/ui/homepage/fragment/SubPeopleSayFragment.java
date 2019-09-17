package com.chengzi.app.ui.homepage.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.PeopleSayAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSubPeopleSayBinding;
import com.chengzi.app.event.PeopleSayAskEvent;
import com.chengzi.app.ui.goods.activity.PeopleSaysActivity;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.ui.homepage.viewmodel.SubPeopleSayViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SubPeopleSayFragment extends RefreshableFragment<FragmentSubPeopleSayBinding, SubPeopleSayViewModel>
        implements View.OnClickListener {

    private PeopleSayAdapter peopleSayAdapter;

    public SubPeopleSayFragment() {
    }

    public static SubPeopleSayFragment newInstance(FragmentManager fm, String targetId, int type) {
        SubPeopleSayFragment fragment = (SubPeopleSayFragment) fm.findFragmentByTag(SubPeopleSayFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putInt(Sys.EXTRA_TARGET_TYPE, type);
            args.putString(Sys.EXTRA_TARGET_ID, targetId);
            fragment = new SubPeopleSayFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sub_people_say;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        int targetType = getArguments().getInt(Sys.EXTRA_TARGET_TYPE, 1);
        String targetId = getArguments().getString(Sys.EXTRA_TARGET_ID);
        viewModel.setTargetType(targetType);
        viewModel.setTargetId(targetId);

        binding.setListener(this);
        initPeopleSay();

        viewModel.peopleSayQuestionLive.observe(this, peopleSayQuestionBeans -> {
            refreshComplete = true;
            peopleSayAdapter.setNewData(peopleSayQuestionBeans);
            finishRefresh();
            if (peopleSayAdapter.getData().isEmpty()) {
                View empty = getLayoutInflater().inflate(R.layout.empty_sub_people_say_layout, binding.recyclerPeopleSay, false);
                peopleSayAdapter.setEmptyView(empty);
                empty.setOnClickListener(this);
            }
        });
    }

    private void initPeopleSay() {
        peopleSayAdapter = new PeopleSayAdapter();
        peopleSayAdapter.bindToRecyclerView(binding.recyclerPeopleSay);
//        binding.recyclerPeopleSay.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        peopleSayAdapter.setOnItemClickListener((adapter, view, position) -> {

            jump2PeopleSay();
        });
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.getPeopleSayList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_sub_people_say_layout:
            case R.id.btn_view_all_people_say:
                jump2PeopleSay();
                break;
        }
    }

    private void jump2PeopleSay() {
        String questionAbout = "";


        if (viewModel.getTargetType() == 2 || viewModel.getTargetType() == 3) {

            DoctorHomePageViewModel doctorInfoViewModel = ViewModelProviders.of(getActivity()).get(DoctorHomePageViewModel.class);
            DoctorHomeInfoBean homeInfoBean = doctorInfoViewModel.doctorHomeLive.getValue();
            if (homeInfoBean != null) {
                String username = homeInfoBean.getUsername();
                String occupation = homeInfoBean.getOccupation();
                questionAbout = username + "，" + occupation;
            }

        } else if (viewModel.getTargetType() == 1) {

            GoodsDetailViewModel goodsDetailViewModel = ViewModelProviders.of(getActivity()).get(GoodsDetailViewModel.class);
            GoodDetailBean goodDetailBean = goodsDetailViewModel.goodDetailLive.getValue();
            if (goodDetailBean != null) {
                String name = goodDetailBean.getName();
                questionAbout = name;
            }
        } else {    //机构-4
            HospitalHomePageViewModel hospitalHomePageViewModel = ViewModelProviders.of(getActivity()).get(HospitalHomePageViewModel.class);
            HospitalHomeInfoBean value = hospitalHomePageViewModel.hospitalDetailLive.getValue();
            questionAbout = value.getInfo().getName();
        }

        PeopleSaysActivity.start(getActivity(), viewModel.getTargetId(),
                viewModel.getTargetType(), questionAbout);
    }

    @Subscribe
    public void updatePeopleSayQuestion(PeopleSayAskEvent event) {
        if (TextUtils.equals(event.getTargetId(), viewModel.getTargetId()) &&
                event.getTargetType() == viewModel.getTargetType()) {
            viewModel.getPeopleSayList();
        }
    }
}
