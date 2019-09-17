package com.chengzi.app.ui.mine.activity.checklook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHospitalCheckLookBindingImpl;
import com.chengzi.app.ui.mine.fragment.HospitalCheckLookFragment;

/**
 * 查看 医院身份
 *
 * @ClassName:HospitalCheckLookActivity
 * @PackageName:com.yimei.app.ui.mine.activity.checklook
 * @Create On 2019/4/11 0011   11:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class HospitalCheckLookActivity extends BaseActivity<ActivityHospitalCheckLookBindingImpl, BaseViewModel> {
    private final String[] TITLES = {"下单", "评价", "投诉", "对话量"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_check_look;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new CheckLookAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private class CheckLookAdapter extends FragmentPagerAdapter {
        public CheckLookAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return HospitalCheckLookFragment.newInstance(position);
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