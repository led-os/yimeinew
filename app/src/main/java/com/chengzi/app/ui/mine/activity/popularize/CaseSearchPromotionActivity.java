package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.umeng.analytics.MobclickAgent;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCaseSearchPromotionBindingImpl;
import com.chengzi.app.ui.mine.activity.cases.AddCaseActivity;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.fragment.CaseSearchPromotionFragment;
import com.chengzi.app.ui.mine.viewmodel.IWantPopularizeViewModel;

import java.util.List;

/**
 * 个人中心 医院 我要推广 案例站内搜索推广/案例管理
 *
 * @ClassName:CaseSearchPromotionActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   18:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */

public class CaseSearchPromotionActivity extends BaseActivity<ActivityCaseSearchPromotionBindingImpl, IWantPopularizeViewModel> {

    //1 案例管理
    private int form = -1;

    private String[] TITLES = {"皮肤美容", "玻尿酸", "肉毒素", "眼部整形", "鼻部整形", "胸部整形"};//已去除
    private List<PromotionSelectCategoryBean> data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_case_search_promotion;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        form = getIntent().getIntExtra("form", -1);
        mBinding.tvTitle.setText(form == 1 ? "案例管理" : "案例站内搜索推广");
        mBinding.rlAdd.setVisibility(form == 1 ? View.VISIBLE : View.GONE);

        if (form != 1) {
            mBinding.tabLayout.setVisibility(View.VISIBLE);

            showLoading(Sys.LOADING);
            mViewModel.promotionSelectCategory("1");
            mViewModel.promotionSelectCategoryLiveData.observe(this, listResponseBean -> {
                dismissLoading();
                if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                    data = listResponseBean.getData();
                    mBinding.viewPager.setOffscreenPageLimit(data.size());
                    mBinding.viewPager.setAdapter(new CaseSearchPromotionAdapter(getSupportFragmentManager()));
                    mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
                }
            });
        } else {
            mBinding.tabLayout.setVisibility(View.GONE);
            mBinding.viewPager.setOffscreenPageLimit(1);
            mBinding.viewPager.setAdapter(new CaseSearchPromotionAdapter(getSupportFragmentManager()));
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        }
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_add:  // 添加案例
                AddCaseActivity.start(this, 1, "");
                break;
        }
    };

    private class CaseSearchPromotionAdapter extends FragmentPagerAdapter {
        public CaseSearchPromotionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (form != 1) {
                return CaseSearchPromotionFragment.newInstance(data.get(position).getId(), form);
            } else {
                return CaseSearchPromotionFragment.newInstance("-1", form);
            }
        }

        @Override
        public int getCount() {
            if (form == 1)
                return 1;
            else
                return data.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (form == 1)
                return TITLES[0];
            else
                return data.get(position).getName();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}