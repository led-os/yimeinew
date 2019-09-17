package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityChooseGoodsBindingImpl;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.fragment.ChooseGoodsFragment;
import com.chengzi.app.ui.mine.viewmodel.GoodsSearchPromoteViewModel;

import java.util.List;

/**
 * 抢推广-->选择商品
 *
 * @ClassName:ChooseGoodsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/15 0015   11:27
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class ChooseGoodsActivity extends BaseActivity<ActivityChooseGoodsBindingImpl, GoodsSearchPromoteViewModel> {
    //分类id
    private String cate_id;
    //专区类型 （1.普通;2.VIP）
    private String zone_type;
    //选中分类
    private PromotionSelectCategoryBean promotionSelectCategoryBean;
    private List<PromotionSelectCategoryBean> data;

    //选中的商品 position
    private static int position;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_choose_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        cate_id = getIntent().getStringExtra("cate_id");
        zone_type = getIntent().getStringExtra("zone_type");
        position = getIntent().getIntExtra("position", 0);

        //tabLayout禁止切换

        mBinding.viewPager.setScanScroll(false);
        showLoading(Sys.LOADING);
        mViewModel.promotionSelectCategory(zone_type);
        mViewModel.promotionSelectCategoryLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                data = listResponseBean.getData();
                mBinding.viewPager.setOffscreenPageLimit(data.size());
                mBinding.viewPager.setAdapter(new ChooseGoodsAdapter(getSupportFragmentManager()));
                mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId().equals(cate_id)) {
                        promotionSelectCategoryBean = data.get(i);
                        mBinding.tabLayout.getTabAt(i).select();
                        break;
                    }
                }

            }
        });
    }

    private class ChooseGoodsAdapter extends FragmentPagerAdapter {
        public ChooseGoodsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int positionss) {
            return ChooseGoodsFragment.newInstance(data.get(positionss).getId(), position, cate_id);
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
