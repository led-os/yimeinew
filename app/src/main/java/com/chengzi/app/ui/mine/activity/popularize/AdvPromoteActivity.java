package com.chengzi.app.ui.mine.activity.popularize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAdvPromoteBindingImpl;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.fragment.AdvPromoteFragment;
import com.chengzi.app.ui.mine.viewmodel.AdvPromoteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心 医院 我要推广  banner广告推广
 *
 * @ClassName:AdvPromoteActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   10:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class AdvPromoteActivity extends BaseActivity<ActivityAdvPromoteBindingImpl, AdvPromoteViewModel> {
    //普通专区分类 和 VIP专区分类
    private List<PromotionSelectCategoryBean> advData = new ArrayList<>();
    private List<PromotionSelectCategoryBean> advVipData = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_adv_promote;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //定位
        String city_name = AccountHelper.getCity_Name();
        if (!TextUtils.isEmpty(city_name))
            mBinding.tvCity.setText(city_name);
        //普通专区和VIP专区
        showLoading(Sys.LOADING);
        mViewModel.advCategory();
        mViewModel.advVipCategory();
        mViewModel.categoryLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                advData = listResponseBean.getData();
                //普通专区
                mBinding.viewPager.setOffscreenPageLimit(advData.size());
                mBinding.viewPager.setAdapter(new AdvPromoteAdapter(getSupportFragmentManager()));
                mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
            }
        });
        mViewModel.categoryVipLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                advVipData = listResponseBean.getData();
                //VIP区
                mBinding.viewPagerVip.setOffscreenPageLimit(advVipData.size());
                mBinding.viewPagerVip.setAdapter(new AdvPromoteAdapter1(getSupportFragmentManager()));
                mBinding.tabLayoutVip.setupWithViewPager(mBinding.viewPagerVip);
            }
        });
    }

    private class AdvPromoteAdapter extends FragmentPagerAdapter {
        public AdvPromoteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PromotionSelectCategoryBean bean = advData.get(position);
            return AdvPromoteFragment.newInstance(bean.getId(), "common", "1", bean.getName());
        }

        @Override
        public int getCount() {
            return advData.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return advData.get(position).getName();
        }
    }

    private class AdvPromoteAdapter1 extends FragmentPagerAdapter {
        public AdvPromoteAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PromotionSelectCategoryBean bean = advVipData.get(position);
            return AdvPromoteFragment.newInstance(bean.getId(), "vip", "2", bean.getName());
        }

        @Override
        public int getCount() {
            return advVipData.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return advVipData.get(position).getName();
        }
    }
}