package com.chengzi.app.ui.vip.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentVipBinding;
import com.chengzi.app.databinding.SearchTabViewBinding;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.event.VipEvent;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.rankinglist.activity.RankingListActivity;
import com.chengzi.app.ui.search.activity.SearchActivity;
import com.chengzi.app.ui.vip.bean.VipBannerListBean;
import com.chengzi.app.ui.vip.viewmodel.VipViewModel;
import com.handong.framework.base.BaseFragment;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * vip
 *
 * @ClassName:VipFragment
 * @PackageName:com.yimei.app.ui.vip.fragment
 * @Create On 2019/3/25 0025   16:10
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/25 0025 handongkeji All rights reserved.
 */

public class VipFragment extends BaseFragment<FragmentVipBinding, VipViewModel>
        implements View.OnClickListener {

    //需要刷新banner
//    private String cate_id;

    public VipFragment() {
    }

    public static VipFragment newInstance() {
        Bundle args = new Bundle();
        VipFragment fragment = new VipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_vip;
    }

    @Subscribe(sticky = true)
    public void reciveStick(VipEvent event) {
        EventBus.getDefault().removeStickyEvent(VipEvent.class);
        String selectCategoryId = event.getVipCategoryId();
        viewModel.currentCategory.setValue(new CategoryItem(selectCategoryId, null));

        if (getView() != null) {
            for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
                Object tag = binding.tabLayout.getTabAt(i).getTag();
                if (tag == null) {
                    continue;
                }
                CategoryItem categoryItem = (CategoryItem) binding.tabLayout.getTabAt(i).getTag();
                if (TextUtils.equals(categoryItem.getId(),event.getVipCategoryId())) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(i),true);
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        binding.setListener(this);

        initSwipeRefreshView();
        //初始化分类 tab
        initTab();
        binding.viewPager.setAdapter(new ViewPager2Adapter(getChildFragmentManager()));
        observe();

        //初始化广告
        initBanner();

        //获取分类
        viewModel.getVipCategory();

        viewModel.refreshObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });
    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event) {
        viewModel.getVipBanner();
    }

    private void initSwipeRefreshView() {
        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            binding.swipeRefreshView.setEnabled(verticalOffset >= 0);
        });
        binding.swipeRefreshView.setColorSchemeResources(R.color.colorAccent);
        binding.swipeRefreshView.setOnRefreshListener(() -> {
            viewModel.getVipCategory();
        });
    }


    //初始化分类 tab
    private void initTab() {
        binding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                CategoryItem bean = (CategoryItem) tab.getTag();
                viewModel.currentCategory.postValue(bean);
                binding.viewPager.setCurrentItem(tab.getPosition() > 0 ? 1 : 0);
                if (tab.getPosition() > 0) {
                    binding.swipeRefreshView.setRefreshing(true);
                }
            }

        });
    }

    //接口回调
    private void observe() {
        //获取分类
        viewModel.vipCategoryLive.observe(this, categoryItems -> {

            if (categoryItems == null || categoryItems.isEmpty()) {
                return;
            }

            binding.tabLayout.removeAllTabs();
            for (CategoryItem categoryItem : categoryItems) {
                SearchTabViewBinding tabViewBinding = DataBindingUtil.inflate(getLayoutInflater(),
                        R.layout.search_tab_view, binding.tabLayout, false);
                tabViewBinding.setBean(categoryItem);
                tabViewBinding.textView.setText(categoryItem.getTypeText());
                tabViewBinding.textView.setTextColor(Color.WHITE);
                tabViewBinding.executePendingBindings();
                CustomTabLayout.Tab tab = binding.tabLayout.newTab()
                        .setCustomView(tabViewBinding.getRoot())
                        .setTag(categoryItem);
                binding.tabLayout.addTab(tab, false);
            }

            if (viewModel.currentCategory.getValue() == null) {
                if (!categoryItems.isEmpty()) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true);
                    CategoryItem item = categoryItems.get(0);
                    viewModel.currentCategory.postValue(item);
                }
            } else {
                CustomTabLayout.Tab selectedTab = null;
                boolean exist = false;
                CategoryItem currentItem = viewModel.currentCategory.getValue();
                for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
                    CustomTabLayout.Tab tab = binding.tabLayout.getTabAt(i);
                    CategoryItem categoryItem = (CategoryItem) tab.getTag();
                    if (TextUtils.equals(currentItem.getId(), categoryItem.getId())) {
                        exist = true;
                        selectedTab = tab;
                        break;
                    }
                }
                if (exist) {
                    final CustomTabLayout.Tab sTab = selectedTab;
                    binding.tabLayout.post(() -> binding.tabLayout.selectTab(sTab, true));
                    viewModel.currentCategory.postValue((CategoryItem) selectedTab.getTag());
                } else {
                    if (!categoryItems.isEmpty()) {
                        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true);
                        binding.viewPager.setCurrentItem(0);
                        CategoryItem item = categoryItems.get(0);
                        viewModel.currentCategory.postValue(item);
                    }
                }

            }
            viewModel.alwaysAliveObservable.update();

        });

        //banner图
        viewModel.getVipBannerLive.observe(this, vipBannerListBeans -> {
            dismissLoading();
            if (vipBannerListBeans != null && vipBannerListBeans.size() > 0) {
                binding.banner.setVisibility(View.VISIBLE);
                initBanner();
                binding.banner.setImages(vipBannerListBeans)
                        .start();
                binding.banner.setOnBannerListener(position -> {
                    String hospital_id = vipBannerListBeans.get(position).getHospital_id();
                    if (!TextUtils.isEmpty(hospital_id)) {
                        ///点击广告扣费
                        viewModel.promotionCutBanner(vipBannerListBeans.get(position).getId());
                        HospitalHomePageActivity.start(getActivity(), hospital_id);
                    }
                });
            } else {
                binding.banner.setVisibility(View.GONE);
//                List<String> strings = new ArrayList<>();
//                binding.banner.update(strings);
//                binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                        .setDelayTime(3000)
//                        .isAutoPlay(true)
//                        .setImageLoader(new ImageLoader() {
//                            @Override
//                            public void displayImage(Context context, Object path, ImageView imageView) {
//                                com.nevermore.oceans.uits.ImageLoader.
//                                        loadImage(imageView, R.drawable.zanwu_long);
//                            }
//                        });
            }
        });

        viewModel.currentCategory.observe(this, categoryItem -> {
            viewModel.getVipBanner();
        });
    }


    //初始化banner图
    private void initBanner() {
        //分类下的->广告
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(3000)
                .isAutoPlay(true)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        com.nevermore.oceans.uits.ImageLoader.
                                loadImage(imageView, ((VipBannerListBean) path).getBanner_img());
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onHiddenChanged(hidden);
                }
            }
        }
        if (hidden) {
            binding.banner.releaseBanner();
        } else {
            binding.banner.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.banner.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.banner.releaseBanner();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:   //  搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.btn_ranking_list:  //  排行榜
                startActivity(new Intent(getActivity(), RankingListActivity.class));
                break;
            case R.id.btn_people_say:  //  大家说
                EventBus.getDefault().post(new SwitchMainTabEvent(3));
//                RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 3);
                break;
            case R.id.btn_refer: //  私享咨询
                PrivateReferActivity.start(getActivity());
                break;
        }
    }

    private class ViewPager2Adapter extends FragmentPagerAdapter {

        public ViewPager2Adapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SelectionVipFragment.newInstance();
                case 1:
                    return NormalVipFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}