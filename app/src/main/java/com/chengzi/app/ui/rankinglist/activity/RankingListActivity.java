package com.chengzi.app.ui.rankinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityRankingListBinding;
import com.chengzi.app.ui.find.activity.FindDetailActivity;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.ui.rankinglist.fragment.CaseRankingListFragment;
import com.chengzi.app.ui.rankinglist.fragment.CounsolerRankingListFragment;
import com.chengzi.app.ui.rankinglist.fragment.DoctorRankingListFragment;
import com.chengzi.app.ui.rankinglist.fragment.GoodsRankingListFragment;
import com.chengzi.app.ui.rankinglist.fragment.OrgRankingListFragment;
import com.chengzi.app.ui.rankinglist.fragment.SearchRankingListFragment;
import com.chengzi.app.ui.rankinglist.viewmodel.RankingListViewModel;
import com.chengzi.app.utils.DateUtils;

/**
 * 排行榜
 *
 * @ClassName:RankingListActivity
 * @PackageName:com.yimei.app.ui.rankinglist.activity
 * @Create On 2019/4/4 0004   10:04
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class RankingListActivity extends BaseActivity<ActivityRankingListBinding, RankingListViewModel> {

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, RankingListActivity.class);
        intent.putExtra(Sys.EXTRA, type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        //本周榜单
        mBinding.setStart(DateUtils.getWeekStart());
        mBinding.setEnd(DateUtils.getWeekEnd());

        observe();

        mBinding.viewPager.setOffscreenPageLimit(6);
        mBinding.viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        mBinding.tabLayoutRole.setupWithViewPager(mBinding.viewPager);

        mBinding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                CategoryItem categoryItem = (CategoryItem) tab.getTag();
                mViewModel.categoryIdLive.postValue(categoryItem);
            }
        });

        mBinding.tabLayoutRole.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                mBinding.tabLayout.setVisibility(tab.getPosition() == 4 ? View.GONE : View.VISIBLE);
            }
        });

        switchTab();

        mViewModel.categoryList();
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_rank:  //  排行榜-->查看规则  6-排行榜规则
                LookVipIntroduceActivity.start(this, "6");
                break;
        }
    };

    private void switchTab() {
        int type = getIntent().getIntExtra(Sys.EXTRA, 0);
        switch (type) {
            case FindDetailActivity.FIND_DOCTOR:
                mBinding.viewPager.setCurrentItem(0);
                break;
            case FindDetailActivity.FIND_GOODS:
                mBinding.viewPager.setCurrentItem(1);
                break;
            case FindDetailActivity.FIND_ORG:
                mBinding.viewPager.setCurrentItem(2);
                break;
            case FindDetailActivity.FIND_COUNSOLER:
                mBinding.viewPager.setCurrentItem(3);
                break;
            case FindDetailActivity.FIND_CASE:
                mBinding.viewPager.setCurrentItem(5);
                break;
            default:
                mBinding.viewPager.setCurrentItem(0);
                break;
        }
    }

    private void observe() {
        mViewModel.categoryLive.observe(this, categoryItems -> {
            mBinding.tabLayout.removeAllTabs();
//            if (categoryItems.size() > 0) {
//                categoryItems.remove(0);
//            }
            if (categoryItems == null) {
                return;
            }
            for (CategoryItem categoryItem : categoryItems) {
                CustomTabLayout.Tab tab = mBinding.tabLayout.newTab()
                        .setText(categoryItem.getName())
                        .setTag(categoryItem);
                mBinding.tabLayout.addTab(tab);
            }
        });

    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        String[] title = {"医生", "商品", "机构", "咨询师", "搜索", "案例"};

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return DoctorRankingListFragment.newInstance();
                case 1:
                    return GoodsRankingListFragment.newInstance();
                case 2:
                    return OrgRankingListFragment.newInstance();
                case 3:
                    return CounsolerRankingListFragment.newInstance();
                case 4:
                    return SearchRankingListFragment.newInstance();
                case 5:
                    return CaseRankingListFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
