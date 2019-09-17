package com.chengzi.app.ui.homepage.hospital.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.DoctorListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDoctorListBinding;
import com.chengzi.app.ui.homepage.viewmodel.DoctorListViewModel;

/**
 * 医院 医生/咨询师列表
 *
 * @ClassName:DoctorListActivity
 * @PackageName:com.yimei.app.ui.homepage.hospital.activity
 * @Create On 2019/4/19 0019   14:23
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class DoctorListActivity extends BaseActivity<ActivityDoctorListBinding, DoctorListViewModel> {

    public static void start(Context context, String hospitalId, int userType) {
        Intent intent = new Intent(context, DoctorListActivity.class);
        intent.putExtra(Sys.EXTRA_HOSPITAL_ID, hospitalId)
                .putExtra(Sys.EXTRA_USER_TYPE, userType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        DoctorListAdapter adapter = new DoctorListAdapter(mViewModel.getUserType());
        adapter.setBeGoodAt(1);
        mBinding.swipeRefreshView.setAdapter(adapter);

        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });

        helper.start();
    }

    private void parseIntent() {
        int type = getIntent().getIntExtra(Sys.EXTRA_USER_TYPE, 2);
        if (type == 3) {
            mBinding.topBar.setCenterText("医院咨询师列表");
        } else {
            mBinding.topBar.setCenterText("医院医生列表");
        }
        mViewModel.setUserType(type);
        String hospitalId = getIntent().getStringExtra(Sys.EXTRA_HOSPITAL_ID);
        mViewModel.setHospitalId(hospitalId);

    }
}
