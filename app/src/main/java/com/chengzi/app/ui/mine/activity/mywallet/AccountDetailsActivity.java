package com.chengzi.app.ui.mine.activity.mywallet;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityAccountDetailsBindingImpl;
import com.chengzi.app.ui.mine.fragment.AccountDeailsFragment;

/**
 * 账户明细
 *
 * @ClassName:AccountDetailsActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   15:47
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class AccountDetailsActivity extends BaseActivity<ActivityAccountDetailsBindingImpl, BaseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_account_details;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.viewPager.setAdapter(new AccountDeailsPagerAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    static final String[] TITLES = {"提现", "补给金"};

    private class AccountDeailsPagerAdapter extends FragmentPagerAdapter {
        public AccountDeailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return AccountDeailsFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
