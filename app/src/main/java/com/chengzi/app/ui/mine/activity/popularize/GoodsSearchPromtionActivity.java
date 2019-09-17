package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityGoodsSearchPromtionBindingImpl;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.fragment.GoodsSearchPromoteFragment;
import com.chengzi.app.ui.mine.viewmodel.GoodsSearchPromoteViewModel;

import java.util.List;

/**
 * 个人中心 医院 我要推广 商品站内搜索推广
 *
 * @ClassName:GoodsSearchPromtionActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   14:12
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */

public class GoodsSearchPromtionActivity extends BaseActivity<ActivityGoodsSearchPromtionBindingImpl, GoodsSearchPromoteViewModel> {
    //分类
    private List<PromotionSelectCategoryBean> data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_goods_search_promtion;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        showLoading(Sys.LOADING);
        mViewModel.promotionSelectCategory("1");
        mViewModel.promotionSelectCategoryLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                data = listResponseBean.getData();
                mBinding.viewPager.setOffscreenPageLimit(data.size());
                mBinding.viewPager.setAdapter(new GoodsSearchPromoteAdapter(getSupportFragmentManager()));
                mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
            }
        });
    }

    private class GoodsSearchPromoteAdapter extends FragmentPagerAdapter {
        public GoodsSearchPromoteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //分类id
            return GoodsSearchPromoteFragment.newInstance(data.get(position).getId());
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
