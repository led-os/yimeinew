package com.chengzi.app.ui.mine.activity.checklook;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCheckLookBindingImpl;
import com.chengzi.app.ui.mine.fragment.CheckComplaintsFragment;
import com.chengzi.app.ui.mine.fragment.CheckLookFragment;
import com.chengzi.app.ui.mine.fragment.CheckPlaceOrderFragment;

/**
 * 查看 医生/咨询师
 *
 * @ClassName:CheckLookActivity
 * @PackageName:com.yimei.app.ui.mine.activity.checklook
 * @Create On 2019/4/10 0010   09:23
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class CheckLookActivity extends BaseActivity<ActivityCheckLookBindingImpl, BaseViewModel> {
    private final String[] TITLES0 = {"预约我的", "投诉我的", "下单我的"};
    private final String[] TITLES1 = {"投诉我的", "下单我的"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_check_look;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {   //医生
            mBinding.viewPager.setOffscreenPageLimit(TITLES0.length);
            mBinding.viewPager.setAdapter(new CheckLookAdapter(getSupportFragmentManager()));
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        } else {  //咨询师
            mBinding.viewPager.setOffscreenPageLimit(TITLES1.length);
            mBinding.viewPager.setAdapter(new CheckLookAdapter(getSupportFragmentManager()));
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        }
    }

    private class CheckLookAdapter extends FragmentPagerAdapter {
        public CheckLookAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                if (position == 0)        // 预约我的
                    return CheckLookFragment.newInstance();
                else if (position == 1)   // 投诉我的
                    return CheckComplaintsFragment.newInstance();
                else  // (position == 2)   // 下单我的
                    return CheckPlaceOrderFragment.newInstance();
            } else {
                if (position == 0)        // 投诉我的
                    return CheckComplaintsFragment.newInstance();
                else  // (position == 1)   // 下单我的
                    return CheckPlaceOrderFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {   //医生
                return TITLES0.length;
            } else {  //咨询师
                return TITLES1.length;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {   //医生
                return TITLES0[position];
            } else {  //咨询师
                return TITLES1[position];
            }
        }
    }
}