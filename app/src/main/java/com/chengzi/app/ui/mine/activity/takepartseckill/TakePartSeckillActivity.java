package com.chengzi.app.ui.mine.activity.takepartseckill;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityTakePartSeckillBindingImpl;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.mine.fragment.TakePartSeckillFragment;
import com.chengzi.app.ui.mine.viewmodel.TakePartSeckillViewModel;

import java.util.List;

/**
 * 参与秒杀
 *
 * @ClassName:TakePartSeckillActivity
 * @PackageName:com.yimei.app.ui.mine.activity.takepartseckill
 * @Create On 2019/4/12 0012   10:09
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */

public class TakePartSeckillActivity extends BaseActivity<ActivityTakePartSeckillBindingImpl, TakePartSeckillViewModel> {
    private List<CategoryItem> data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_take_part_seckill;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //分类
        mViewModel.categoryList();
        mViewModel.categoryLive.observe(this, navigatorListBean -> {
            data = navigatorListBean;
            if (data == null || data.isEmpty()) {
                return;
            }
            mBinding.viewPager.setOffscreenPageLimit(data.size());
            mBinding.viewPager.setAdapter(new TakePartSeckillAdapter(getSupportFragmentManager()));
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        });
    }

    private class TakePartSeckillAdapter extends FragmentPagerAdapter {
        public TakePartSeckillAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TakePartSeckillFragment.newInstance(data.get(position).getId());
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getName();
        }
    }
}