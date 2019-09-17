package com.chengzi.app.ui.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityAppointmentBinding;
import com.chengzi.app.dialog.TestDriveTimeDialog;
import com.chengzi.app.ui.homepage.bean.BelongOrgBean;
import com.chengzi.app.ui.homepage.bean.ChooseAppointmentBean;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.viewmodel.AppointmentViewModel;

import java.util.Calendar;

/**
 * 预约 医生/医院
 *
 * @ClassName:AppointmentActivity
 * @PackageName:com.yimei.app.ui.homepage.activity
 * @Create On 2019/4/18 0018   19:05
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class AppointmentActivity extends BaseActivity<ActivityAppointmentBinding, AppointmentViewModel>
        implements View.OnClickListener {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_appointment;
    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    //预约医生
    public static void start(Context context, DoctorHomeInfoBean homeInfoBean) {
        context.startActivity(new Intent(context, AppointmentActivity.class)
                .putExtra("homeInfoBean", homeInfoBean)
        );
    }

    //预约医院
    public static void start(Context context, BelongOrgBean belongOrg) {
        context.startActivity(new Intent(context, AppointmentActivity.class)
                .putExtra("belongOrg", belongOrg)
        );
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        BelongOrgBean bean = (BelongOrgBean) getIntent().getSerializableExtra("belongOrg");
        if (bean != null) { //预约医院
            mBinding.setHospitalBean(bean);
            mViewModel.setTargetId(bean.getId());
        } else {
            DoctorHomeInfoBean homeInfoBean = (DoctorHomeInfoBean) getIntent().getSerializableExtra("homeInfoBean");
            mBinding.setDoctorBean(homeInfoBean);
            mViewModel.setTargetId(homeInfoBean == null ? "" : homeInfoBean.getId());
        }

//        EventBus.getDefault().register(this);

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mViewModel.postAppointmentLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getData() != null) {
                ChooseAppointmentBean data = responseBean.getData();
                String is_exist = data.getIs_exist();
                String is_update = data.getIs_update();
                if (is_exist.equals("0")) {
                    toast("当前时段不可预约");
                } else if (is_update.equals("0")) {
                    toast("预约失败");
                } else {
                    ToastUtils.showShort("提交成功");
                    finish();
                }
            }
        });
    }

//    @Subscribe(sticky = true)
//    public void getDoctorInfo(DoctorHomeInfoBean doctorInfo) {
//        mBinding.setDoctorBean(doctorInfo);
//        mViewModel.setTargetId(doctorInfo.getId());
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pick_appointment_time:
                TestDriveTimeDialog testDriveTimeDialog = new TestDriveTimeDialog();
                testDriveTimeDialog.setListener((year, month, day, hour) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month - 1, day, hour, 0, 0);
                    mViewModel.setAppointmentTime(calendar.getTimeInMillis());
                    mBinding.tvAppointmentTime.setText(year + "年" + month + "月" + day + "日" + " " + hour + ":00");
                });
                testDriveTimeDialog.show(getSupportFragmentManager(), TestDriveTimeDialog.class.getSimpleName());
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(mViewModel.name.get())) {
                    ToastUtils.showShort("请填写姓名");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.mobile.get())) {
                    ToastUtils.showShort("请填写联系电话");
                    return;
                }
                if (!RegexUtils.isMobileSimple(mViewModel.mobile.get())) {
                    ToastUtils.showShort("联系电话有误");
                    return;
                }

                if (mViewModel.getAppointmentTime() <= 0) {
                    ToastUtils.showShort("请选择预约时间");
                    return;
                }

                if (TextUtils.isEmpty(mViewModel.supplement.get())) {
                    ToastUtils.showShort("请填写变美需求");
                    return;
                }

                showLoading("");
                mViewModel.postAppointment();
                break;
        }
    }
}
