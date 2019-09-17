package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityZonePromoteBindingImpl;
import com.chengzi.app.ui.mine.fragment.ZonePromoteFragment;

/**
 * 专区推广 普通/VIP
 *
 * @ClassName:ZonePromoteActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/15 0015   10:16
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class ZonePromoteActivity extends BaseActivity<ActivityZonePromoteBindingImpl, BaseViewModel> {
    // 专区类型 1.普通;2.VIP
    private final String[] TITLES = {"普通区推广", "VIP区推广"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_zone_promote;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //沉浸式状态栏
//        StatusBarUtil.transparencyBar(this);
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new ZonePromoteAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.ivFinish.setOnClickListener(v -> {
            finish();
        });
    }

    private class ZonePromoteAdapter extends FragmentPagerAdapter {
        public ZonePromoteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ZonePromoteFragment.newInstance(String.valueOf(position + 1));
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
