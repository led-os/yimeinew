package com.chengzi.app.ui.homepage.doctor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentAppointmentBinding;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.AppointmentManageBean;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;
import com.chengzi.app.ui.mine.activity.VisitTimeManagerActivity;

/**
 * 医生可预约时间
 *
 * @ClassName:AppointmentFragment
 * @PackageName:com.yimei.app.ui.homepage.doctor.fragment
 * @Create On 2019/4/18 0018   14:08
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class AppointmentFragment extends RefreshableFragment<FragmentAppointmentBinding, DoctorHomePageViewModel>
        implements View.OnClickListener {

    public AppointmentFragment() {
    }

    public static AppointmentFragment newInstance(FragmentManager fm) {
        AppointmentFragment fragment = (AppointmentFragment) fm.findFragmentByTag(AppointmentFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new AppointmentFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_appointment;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.isSelf())
            viewModel.doctorHomeInfo();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);

        viewModel.doctorHomeLive.observe(this, doctorHomeInfoBean -> {
            AppointmentManageBean appointment_manage = doctorHomeInfoBean.getAppointment_manage();
            if (appointment_manage == null) {
                return;
            }
            binding.getRoot().setVisibility(View.VISIBLE);
            AppointmentManageBean.DayBean today = appointment_manage.getToday();
            AppointmentManageBean.DayBean tomorrow = appointment_manage.getTomorrow();
            AppointmentManageBean.DayBean the_day_after_tomorrow = appointment_manage.getThe_day_after_tomorrow();

            binding.tvTodayAm.setText(getString(R.string.hand_morning) +
                    (today.isAm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvTodayAm.setEnabled(today.isAm());
            binding.tvTodayPm.setText(getString(R.string.hand_afternoon) +
                    (today.isPm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvTodayPm.setEnabled(today.isPm());


            binding.tvTomorrowAm.setText(getString(R.string.hand_morning) +
                    (tomorrow.isAm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvTomorrowAm.setEnabled(tomorrow.isAm());
            binding.tvTomorrowPm.setText(getString(R.string.hand_afternoon) +
                    (tomorrow.isPm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvTomorrowPm.setEnabled(tomorrow.isPm());


            binding.tvAfterTomorrowAm.setText(getString(R.string.hand_morning) +
                    (the_day_after_tomorrow.isAm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvAfterTomorrowAm.setEnabled(the_day_after_tomorrow.isAm());
            binding.tvAfterTomorrowPm.setText(getString(R.string.hand_afternoon) +
                    (the_day_after_tomorrow.isPm() ? getString(R.string.bookable) : getString(R.string.unbookable)));
            binding.tvAfterTomorrowPm.setEnabled(the_day_after_tomorrow.isPm());
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_all: //TODO：传被查看的医生的user_id
                VisitTimeManagerActivity.start(getContext(), viewModel.getDoctorId());
                break;
        }
    }
}
