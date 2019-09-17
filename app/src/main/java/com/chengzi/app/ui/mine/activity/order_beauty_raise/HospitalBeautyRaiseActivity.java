package com.chengzi.app.ui.mine.activity.order_beauty_raise;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHospitalBeautyRaiseBindingImpl;
import com.chengzi.app.ui.mine.fragment.UserBeautyRaiseFragment;

/**
 * 美人筹 -->医院 (只有普通用户 和 医院)
 *
 * @ClassName:HospitalBeautyRaiseActivity
 * @PackageName:com.yimei.app.ui.mine.activity.order_beauty_raise
 * @Create On 2019/4/3 0003   12:01
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class HospitalBeautyRaiseActivity extends BaseActivity<ActivityHospitalBeautyRaiseBindingImpl, BaseViewModel> {

    // 1-进行中，2-待使用，3-已完成
    private final String[] TITLES = {"进行中", "待使用", "已完成"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_beauty_raise;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new HospitalBeautyRaiseAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private class HospitalBeautyRaiseAdapter extends FragmentPagerAdapter {
        public HospitalBeautyRaiseAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return UserBeautyRaiseFragment.newInstance1(i + 1);
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