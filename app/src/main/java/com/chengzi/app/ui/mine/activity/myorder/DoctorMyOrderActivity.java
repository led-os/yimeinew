package com.chengzi.app.ui.mine.activity.myorder;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityDoctorMyOrderBindingImpl;
import com.chengzi.app.ui.mine.fragment.HospitalOrderListFragment;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;

/**
 * 医院订单管理
 *
 * @ClassName:DoctorMyOrderActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/11 0011   16:46
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */

public class DoctorMyOrderActivity extends BaseActivity<ActivityDoctorMyOrderBindingImpl, MyOrderListViewModel> {

    //机构订单状态 0-全部 1-待使用，2-待评价，3-已完成
    private final String[] TITLES = {"全部", "待使用", "待评价", "已完成"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_my_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new MyOrderAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private class MyOrderAdapter extends FragmentPagerAdapter {
        public MyOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int h_status) {
            return HospitalOrderListFragment.newInstance(h_status);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
