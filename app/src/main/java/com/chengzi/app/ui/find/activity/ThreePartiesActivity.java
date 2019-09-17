package com.chengzi.app.ui.find.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityThreePartiesBindingImpl;
import com.chengzi.app.ui.find.fragment.ThreePartiesFragment;

/**
 * 三方-->人才招聘 行业会议发布 培训发布
 *
 * @ClassName:ThreePartiesActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/4/19 0019   14:08
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class ThreePartiesActivity extends BaseActivity<ActivityThreePartiesBindingImpl, BaseViewModel> {
    //人才招聘 行业会议发布 培训发布
    public static final int THREEPARTIES_RECRUITMENT = 0;
    public static final int THREEPARTIES_MEETING = 1;
    public static final int THREEPARTIES_TRAINING = 2;


    private final String[] TITLES = {"人才招聘", "行业会议发布", "培训发布"};
    private final int[] TITLES_IDS = {1, 2, 3};
    private int extra;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_three_parties;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment");
        if (fragment == null) {
            extra = getIntent().getIntExtra("extra", THREEPARTIES_RECRUITMENT);
        }
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new ThreePartiesAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.ivFinish.setOnClickListener(v -> {
            finish();
        });
        mBinding.tabLayout.setScrollPosition(extra, 0, true);
        mBinding.viewPager.setCurrentItem(extra, false);
    }

    private class ThreePartiesAdapter extends FragmentPagerAdapter {
        public ThreePartiesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ThreePartiesFragment.newInstance(TITLES_IDS[position]);
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