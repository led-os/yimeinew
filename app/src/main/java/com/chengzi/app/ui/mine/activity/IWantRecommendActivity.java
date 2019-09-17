package com.chengzi.app.ui.mine.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityIwantRecommendBindingImpl;
import com.chengzi.app.ui.mine.bean.PromoteRoutingBean;
import com.chengzi.app.ui.mine.fragment.IWantRecommendFragment;
import com.chengzi.app.ui.mine.fragment.PromoteRankFragment;

/**
 * 我要推荐客户 我要推荐/推广排行
 *
 * @ClassName:IWantRecommendActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/15 0015   14:47
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class IWantRecommendActivity extends BaseActivity<ActivityIwantRecommendBindingImpl, BaseViewModel> {
    //如果绑定医院了 到这个页面->并传过来bean实体类
    private PromoteRoutingBean bean;

    private final String[] TITLES = {"我要推荐", "推广排行"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_iwant_recommend;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (PromoteRoutingBean) getIntent().getSerializableExtra("data");
        if (bean == null) {
            toast("您没有绑定机构，请先绑定!");
            finish();
        } else {
            mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
            mBinding.viewPager.setAdapter(new IWantRecommendAdapter(getSupportFragmentManager()));
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        }
    }

    private class IWantRecommendAdapter extends FragmentPagerAdapter {
        public IWantRecommendAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return IWantRecommendFragment.newInstance(bean);
            } else {
                return new PromoteRankFragment();
            }
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
