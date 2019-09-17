package com.chengzi.app.ui.homepage.hospital.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.MainDoctorAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSubDoctorsBinding;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.hospital.activity.DoctorListActivity;
import com.chengzi.app.ui.homepage.hospital.viewmodel.SubDoctorsViewModel;
import com.chengzi.app.ui.mine.activity.DoctorOrCounselorActivity;

/**
 * 医院主页 -- 显示三个医生/咨询师
 *
 * @ClassName:SubDoctorsFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   11:51
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class SubDoctorsFragment extends RefreshableFragment<FragmentSubDoctorsBinding, SubDoctorsViewModel>
        implements View.OnClickListener {

    public SubDoctorsFragment() {
    }

    private boolean isSelf = false;

    /**
     * @param fm
     * @param hospitalId
     * @param type       2 该医院的下医生，3 该医院下的咨询师
     * @param isSelf     true 从个人中心查看自己的主页，false 查看别人的主页
     * @return
     */
    public static SubDoctorsFragment newInstance(FragmentManager fm, String hospitalId, int type, boolean isSelf) {
        Fragment fragment = fm.findFragmentByTag(SubDoctorsFragment.class.getSimpleName() + type);
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putInt(Sys.EXTRA_USER_TYPE, type);
            args.putString(Sys.EXTRA_HOSPITAL_ID, hospitalId);
            args.putBoolean("isSelf", isSelf);
            fragment = new SubDoctorsFragment();
            fragment.setArguments(args);
        }
        return (SubDoctorsFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sub_doctors;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        int type = getArguments().getInt(Sys.EXTRA_USER_TYPE);
        viewModel.setDoctorType(type);
        String hospitalId = getArguments().getString(Sys.EXTRA_HOSPITAL_ID);
        viewModel.setHospitalId(hospitalId);

        isSelf = getArguments().getBoolean("isSelf");
        if (type == 3) {
            binding.tvHint.setText("咨询师展示");
        } else {
            binding.tvHint.setText("医生展示");
        }

        MainDoctorAdapter adapter = new MainDoctorAdapter(type);
        adapter.setAverage(1);
        binding.recyclerDoctor.setAdapter(adapter);

        viewModel.bindedDoctorLive.observe(this, doctorBeans -> {

            refreshComplete = true;
            finishRefresh();

            if (doctorBeans == null || doctorBeans.getData() == null || doctorBeans.getData().isEmpty()) {
                binding.getRoot().setVisibility(View.GONE);
                return;
            }
            binding.getRoot().setVisibility(View.VISIBLE);
            adapter.setNewData(doctorBeans.getData());
            //查看全部？位医生/咨询师
            String type_name = (type == 3) ? "咨询师" : "医生";
            binding.btnViewAllDoctors.setText(getString(R.string.view_all) + doctorBeans.getTotal() + "位" + type_name);
        });
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.getBindedDoctor();
    }

    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_all_doctors: //医生/咨询师全部（别人的机构到列表 自己的机构到管理）
                if (!isSelf) {
                    DoctorListActivity.start(getActivity(), viewModel.getHospitalId(), viewModel.getDoctorType());
                } else {
                    DoctorOrCounselorActivity.start(getContext(), viewModel.getDoctorType());
                }
                break;
        }
    }
}
