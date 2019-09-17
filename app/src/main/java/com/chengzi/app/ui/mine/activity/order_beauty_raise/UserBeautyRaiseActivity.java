package com.chengzi.app.ui.mine.activity.order_beauty_raise;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityUserBeautyRaiseBindingImpl;
import com.chengzi.app.ui.mine.fragment.UserBeautyRaiseFragment;
import com.chengzi.app.ui.peopleraise.activity.PostPeopleRaiseActivity;

/**
 * 美人筹 -->普通用户 (只有普通用户 和 医院)
 *
 * @ClassName:UserBeautyRaiseActivity
 * @PackageName:com.yimei.app.ui.mine.activity.order_beauty_raise
 * @Create On 2019/4/3 0003   09:46
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class UserBeautyRaiseActivity extends BaseActivity<ActivityUserBeautyRaiseBindingImpl, BaseViewModel> {

    //-1全部 0待付款 1未达成 2待使用 3已完成 4已取消
    private final String[] TITLES = {"全部", "待付款", "未达成", "待使用", "已完成"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_beauty_raise;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new UserBeautyRaiseAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        //发布美人筹
        mBinding.topBar.setOnRightClickListener(v -> goActivity(PostPeopleRaiseActivity.class));

    }

    private class UserBeautyRaiseAdapter extends FragmentPagerAdapter {
        public UserBeautyRaiseAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return UserBeautyRaiseFragment.newInstance(i - 1);
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