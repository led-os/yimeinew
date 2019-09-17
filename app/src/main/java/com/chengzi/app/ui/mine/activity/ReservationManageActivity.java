package com.chengzi.app.ui.mine.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityReservationManageBindingImpl;
import com.chengzi.app.ui.mine.fragment.ReservationManageFragment;

/**
 * 预约管理-->医院身份
 *
 * @ClassName:ReservationManageActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/11 0011   10:07
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */

public class ReservationManageActivity extends BaseActivity<ActivityReservationManageBindingImpl, BaseViewModel> {
    private final String[] TITLES = {"医生预约", "医院预约"};
    private final Integer[] TYPES = {2, 4}; //用户类型 2 医生 4 机构 （必传）

    @Override
    public int getLayoutRes() {
        return R.layout.activity_reservation_manage;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new ReservationManageAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private class ReservationManageAdapter extends FragmentPagerAdapter {
        public ReservationManageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ReservationManageFragment.newInstance(TYPES[position]);
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