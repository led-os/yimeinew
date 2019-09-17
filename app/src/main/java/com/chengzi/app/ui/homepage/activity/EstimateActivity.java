package com.chengzi.app.ui.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityEstimateBinding;
import com.chengzi.app.ui.goods.fragment.EstimateFragment;
import com.chengzi.app.ui.goods.viewmodel.EstimateViewModel;

/**
 * 医生/咨询师/医院 评价列表
 *
 * @ClassName:EstimateActivity
 * @PackageName:com.yimei.app.ui.homepage.doctor.activity
 * @Create On 2019/4/18 0018   16:24
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class EstimateActivity extends BaseActivity<ActivityEstimateBinding, EstimateViewModel> {


    public static void start(Context context, String targetId, int targetType) {
        Intent intent = new Intent(context, EstimateActivity.class);
        intent.putExtra(Sys.EXTRA_TARGET_ID, targetId);
        intent.putExtra(Sys.EXTRA_TARGET_TYPE, targetType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_estimate;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        if (mViewModel.getTargetType() == 2) {
            mBinding.topBar.setCenterText(getString(R.string.doctor_evaluate));

        } else if (mViewModel.getTargetType() == 3) {
            mBinding.topBar.setCenterText(getString(R.string.counselor_evaluate));

        } else if (mViewModel.getTargetType() == 4) {
            mBinding.topBar.setCenterText(getString(R.string.hospital_evaluate));

        }

        EstimateFragment fragment = (EstimateFragment) getSupportFragmentManager()
                .findFragmentByTag(EstimateFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = EstimateFragment.newInstance(mViewModel.getTargetId(), mViewModel.getTargetType());
        }
        fragment.setUserVisibleHint(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, EstimateFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    private void parseIntent() {
        int targetType = getIntent().getIntExtra(Sys.EXTRA_TARGET_TYPE, 1);
        String targetId = getIntent().getStringExtra(Sys.EXTRA_TARGET_ID);
        mViewModel.setTargetId(targetId);
        mViewModel.setTargetType(targetType);
    }

}
