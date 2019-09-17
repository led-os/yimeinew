package com.chengzi.app.ui.goods.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentBindedDoctorBinding;
import com.chengzi.app.databinding.ItemGoodDoctorLayoutBinding;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;

import java.util.List;

public class BindedDoctorFragment extends BaseFragment<FragmentBindedDoctorBinding, GoodsDetailViewModel> {

    private DoctorAdapter doctorAdapter;
    private DoctorAdapter counselorAdapter;

    public BindedDoctorFragment() {
    }

    public static BindedDoctorFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(BindedDoctorFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new BindedDoctorFragment();
            fragment.setArguments(args);
        }
        return (BindedDoctorFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_binded_doctor;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        initDoctorAndCounselor();

        viewModel.goodDetailLive.observe(this, goodDetailBean -> {

            List<DoctorBean> doctor_data = goodDetailBean.getDoctor_data();
            List<DoctorBean> counselling_data = goodDetailBean.getCounselling_data();

            if (doctor_data.isEmpty() && counselling_data.isEmpty()) {
                binding.getRoot().setVisibility(View.GONE);
                return;
            }

            binding.getRoot().setVisibility(View.VISIBLE);

            binding.tvResponsbilityDoctor.setVisibility(doctor_data.isEmpty() ? View.GONE : View.VISIBLE);
            binding.recyclerDoctor.setVisibility(doctor_data.isEmpty() ? View.GONE : View.VISIBLE);
            if (!doctor_data.isEmpty()) {
                doctorAdapter.setNewData(doctor_data);
            }

            binding.tvResponsbilityConsultant.setVisibility(counselling_data.isEmpty() ? View.GONE : View.VISIBLE);
            binding.recyclerCounselor.setVisibility(counselling_data.isEmpty() ? View.GONE : View.VISIBLE);
            if (!counselling_data.isEmpty()) {
                counselorAdapter.setNewData(counselling_data);
            }

        });

    }


    private void initDoctorAndCounselor() {
        doctorAdapter = new DoctorAdapter(2);
        binding.recyclerDoctor.setAdapter(doctorAdapter);

        counselorAdapter = new DoctorAdapter(3);
        binding.recyclerCounselor.setAdapter(counselorAdapter);

    }

    private static class DoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder>
            implements ClickEventHandler<DoctorBean> {

        private int userType; //  2 医生   3 咨询师

        public DoctorAdapter(int userType) {
            super(R.layout.item_good_doctor_layout);
            this.userType = userType;
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            ItemGoodDoctorLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(this);
            binding.setBean(item);
        }

        @Override
        public void handleClick(View view, DoctorBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }

            Context context = view.getContext();
            if (userType == 2) {
                DoctorHomePageActivity.startDoctor(context, bean.getId());
            } else {
                DoctorHomePageActivity.startCounselor(context, bean.getId());
            }
        }
    }

}
