package com.chengzi.app.ui.home.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.viewpager2.adapter.FragmentStateAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.AppUtils;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentHomeBinding;
import com.chengzi.app.databinding.SearchTabViewBinding;
import com.chengzi.app.event.NormalCategoryEvent;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.common.activity.WebActivity;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.find.activity.FindActivity;
import com.chengzi.app.ui.home.activity.AppNavigatorActivity;
import com.chengzi.app.ui.home.activity.SelectCityActivity;
import com.chengzi.app.ui.home.bean.AdvListBean;
import com.chengzi.app.ui.home.bean.AdvertisingBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.LocationBean;
import com.chengzi.app.ui.home.viewmodel.HomeViewModel;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.peopleraise.activity.PeopleRaiseActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.rankinglist.activity.RankingListActivity;
import com.chengzi.app.ui.search.activity.SearchActivity;
import com.chengzi.app.utils.PreferenceManager;
import com.handong.framework.base.BaseFragment;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 首页
 *
 * @ClassName:HomeFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/3/25 0025   16:03
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/25 0025 handongkeji All rights reserved.
 */

@RuntimePermissions
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel>
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_CODE_SELECT_CITY = 0x01;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        binding.setListener(this);
        binding.setIsRecommand(viewModel.isRecommand);
        binding.setViewModel(viewModel);

        binding.swipeRefreshView.setColorSchemeResources(com.nevermore.oceans.R.color.colorAccent);
        binding.swipeRefreshView.setOnRefreshListener(this);

        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                binding.swipeRefreshView.setEnabled(true);
            } else {
                binding.swipeRefreshView.setEnabled(false);
            }
        });

        binding.viewPager.setAdapter(new HomeNavigatorAdapter(getChildFragmentManager()));

        initTab();

        configGobalBanner();

        observe();
        viewModel.homeCategory();
        viewModel.advertisement();

        HomeFragmentPermissionsDispatcher.startLocationWithPermissionCheck(this);
        viewModel.cityList();
    }

    @Subscribe
    public void onNavigatorClick(NormalCategoryEvent event) {
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            CustomTabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            CategoryItem categoryItem = (CategoryItem) tab.getTag();
            if (TextUtils.equals(event.getCategoryId(), categoryItem.getId())) {
                binding.tabLayout.selectTab(tab, true);
                break;
            }
        }
    }

    @Override
    public void onRefresh() {

        viewModel.setRefreshing(true);
//        viewModel.positionList();
        viewModel.homeCategory();
        viewModel.advertisement();

        if (binding.viewPager.getCurrentItem() == 0 && recommandFragment != null) {
            recommandFragment.refresh();
        }

    }

    private RecommandFragment recommandFragment;
    private NormalFragment normalFragment;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof RecommandFragment) {
            recommandFragment = (RecommandFragment) childFragment;
        } else if (childFragment instanceof NormalFragment) {
            normalFragment = (NormalFragment) childFragment;
        }
    }

    private void observe() {
        //推荐顶部广告 he 三方
        viewModel.advertisingLive.observe(this, advertisingHolderBean -> {
            List<AdvertisingBean> wholeData = advertisingHolderBean.getWholeData();
            List<AdvertisingBean> thirdData = advertisingHolderBean.getThirdData();
            setUpGobalBanner(wholeData);
            if (recommandFragment != null) {
                recommandFragment.setUpThirdpart(thirdData);
            }
        });
        //分类
        viewModel.homeCategoryLive.observe(this, categoryItems -> {
            if (categoryItems == null) {
                return;
            }
            binding.tabLayout.removeAllTabs();
            for (CategoryItem categoryItem : categoryItems) {
                SearchTabViewBinding tabViewBinding = DataBindingUtil.inflate(getLayoutInflater(),
                        R.layout.search_tab_view, binding.tabLayout, false);
                tabViewBinding.textView.setText(categoryItem.getTypeText());
                tabViewBinding.textView.setTextColor(Color.WHITE);
                tabViewBinding.executePendingBindings();
                CustomTabLayout.Tab tab = binding.tabLayout.newTab()
                        .setCustomView(tabViewBinding.getRoot())
                        .setTag(categoryItem);
                binding.tabLayout.addTab(tab, false);
            }
            if (viewModel.isRefreshing()) {
                if (viewModel.getSelectedCategory() == null) {
                    if (!categoryItems.isEmpty()) {
                        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true);
                        binding.viewPager.setCurrentItem(0);
                        viewModel.setSelectedCategory(categoryItems.get(0));
                        viewModel.positionList();
                    }

                } else {
                    CustomTabLayout.Tab selectedTab = null;
                    boolean exist = false;
                    for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
                        CustomTabLayout.Tab tab = binding.tabLayout.getTabAt(i);
                        CategoryItem categoryItem = (CategoryItem) tab.getTag();
                        if (TextUtils.equals(categoryItem.getId(), viewModel.getSelectedCategory().getId())) {
                            exist = true;
                            selectedTab = tab;
                            break;
                        }
                    }
                    if (exist) {
                        CustomTabLayout.Tab sTab = selectedTab;
                        binding.tabLayout.selectTab(sTab, true);
                        viewModel.setSelectedCategory((CategoryItem) sTab.getTag());
                        viewModel.positionList();

                    } else {
                        if (!categoryItems.isEmpty()) {
                            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true);
                            binding.viewPager.setCurrentItem(0);
                            viewModel.setSelectedCategory(categoryItems.get(0));
                            viewModel.positionList();
                        }
                    }

                }
                if (binding.viewPager.getCurrentItem() == 1 && normalFragment != null) {
                    normalFragment.refresh();
                }
            } else {
                if (!categoryItems.isEmpty()) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true);
                    viewModel.setSelectedCategory(categoryItems.get(0));
                }
                //一级分类下的广告-->推荐
                viewModel.cityAndCategoryStatus[1] = true;
                viewModel.cityAndCategoryInitLive.postValue(true);
            }

        });

        viewModel.cityAndCategoryInitLive.observe(this, aBoolean -> {

            boolean ready = true;
            for (boolean cityAndCategoryStatus : viewModel.cityAndCategoryStatus) {
                ready &= cityAndCategoryStatus;
            }
            if (ready) {
                viewModel.positionList();
                viewModel.changeCity(PreferenceManager.getPreference(PreferenceManager.CITY_ID), false);
            }

        });

        //一级分类下的广告
        viewModel.positionListLive.observe(this, advListBeans -> {
            viewModel.setRefreshing(false);
            binding.swipeRefreshView.setRefreshing(false);

            if (advListBeans != null && advListBeans.size() > 0) {
                binding.banner.setVisibility(View.VISIBLE);
                configBanner();
                binding.banner.setImages(advListBeans)
                        .start();

                binding.banner.setOnBannerListener(position -> {
                    String hospital_id = advListBeans.get(position).getUser_id();
                    if (!TextUtils.isEmpty(hospital_id)) {
                        ///点击广告扣费
                        viewModel.promotionCutBanner(advListBeans.get(position).getId());
                        HospitalHomePageActivity.start(getActivity(), hospital_id);
                    }
                });
            } else {    //没有数据
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
        //切换城市成功！ 用RxBus去刷新数据
        viewModel.changeCityLive.observe(this, responseBean -> {
            dismissLoading();
            EventBus.getDefault().post(new OnCityChangeEvent());
            //刷新本页 (其他页:猜你喜欢 和 商品 和 三方)
            viewModel.advertisement();
            viewModel.positionList();
        });

    }

    private void initTab() {
        binding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                viewModel.isRecommand.set(tab.getPosition() <= 0);
                CategoryItem item = (CategoryItem) tab.getTag();
                viewModel.setSelectedCategory(item);
                if (tab.getPosition() > 0) {
                    binding.viewPager.setCurrentItem(1);
                    if (normalFragment != null) {
                        normalFragment.show(item);
                    }
                } else {
                    binding.viewPager.setCurrentItem(0);
                }
                //一级分类下的广告-->推荐
                viewModel.positionList();

            }

        });
    }

    //  首页顶部广告 绑定数据
    private void setUpGobalBanner(List<AdvertisingBean> list) {
        binding.gobalBanner.setOnBannerListener(position -> {
            if (list != null && position >= 0 && position < list.size()) {
                AdvertisingBean bean = list.get(position);
                WebActivity.start(getActivity(), bean.getUrl());
            }
        });

        binding.gobalBanner.update(list);
        binding.gobalBanner.start();
    }

    private void configBanner() {
        //分类下的->(广告区A1是5张，根据一级分类变，按收费点击规则，每个分类下各有5张（医院购买的广告位默认跳转到机构主页）)
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(3000)
                .isAutoPlay(true)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        com.nevermore.oceans.uits.ImageLoader.
                                loadImage(imageView, ((AdvListBean) path).getBanner_img());
                    }
                });
    }

    private void configGobalBanner() {
        //推荐顶部广告
        binding.gobalBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(3000)
                .isAutoPlay(true)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        com.nevermore.oceans.uits.ImageLoader.
                                loadImage(imageView, ((AdvertisingBean) path).getImage());
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
            binding.gobalBanner.releaseBanner();
            binding.banner.releaseBanner();
        } else {
            binding.gobalBanner.start();
            binding.banner.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.gobalBanner.start();
        binding.banner.start();
        if (viewModel.isLaunchAppDetail()) {
            viewModel.setLaunchAppDetail(false);
            HomeFragmentPermissionsDispatcher.startLocationWithPermissionCheck(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.gobalBanner.releaseBanner();
        binding.banner.releaseBanner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_app_navigator:   //  app 导航
                startActivity(new Intent(getActivity(), AppNavigatorActivity.class));
                break;
            case R.id.tv_city:   //  选择城市
                startActivityForResult(new Intent(getActivity(), SelectCityActivity.class)
                                .putExtra("location", viewModel.location.get()),
                        REQUEST_CODE_SELECT_CITY, data -> {
                            CityBean cityBean = (CityBean) data.getSerializableExtra("city");
                            viewModel.cityName.set(cityBean.getCity_name());
                            PreferenceManager.setPreference(PreferenceManager.CITY_ID, cityBean.getCity_id());
                            PreferenceManager.setPreference(PreferenceManager.CITY_NAME, cityBean.getCity_name());
                            viewModel.changeCity(cityBean.getCity_id());
                        });
                break;
            case R.id.btn_search_normal:
            case R.id.btn_search:   //  搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.btn_look_for:    //  找
                startActivity(new Intent(getActivity(), FindActivity.class));
                break;
            case R.id.btn_ranking_list:  //  排行榜
                startActivity(new Intent(getActivity(), RankingListActivity.class));
                break;
            case R.id.btn_people_say:  //  大家说
                EventBus.getDefault().post(new SwitchMainTabEvent(3));
//                RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 3);
                break;
            case R.id.btn_refer:  //  私享咨询

                PrivateReferActivity.start(getActivity());
                break;
            case R.id.btn_people_raise: //  美人筹
                startActivity(new Intent(getActivity(), PeopleRaiseActivity.class));
                break;
        }
    }

    @NeedsPermission(value = {Manifest.permission.ACCESS_FINE_LOCATION})
    void startLocation() {
        AMapLocationClient client = new AMapLocationClient(getActivity().getApplicationContext());
        client.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                client.unRegisterLocationListener(this);
                if (location != null && location.getErrorCode() == 0) {

                    String cityCode = location.getCityCode();
                    String city = location.getCity();
                    String province = location.getProvince();
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    PreferenceManager.setPreference(PreferenceManager.LATITUDE, String.valueOf(latitude));
                    PreferenceManager.setPreference(PreferenceManager.LONGITUDE, String.valueOf(longitude));

                    LocationBean locationBean = new LocationBean(province, city);
                    viewModel.location.set(locationBean);
                } else {
                    //  定位失败
                    LocationBean locationBean = new LocationBean(null, null);
                    viewModel.location.set(locationBean);
                }

            }
        });
        AMapLocationClientOption option = new AMapLocationClientOption();
        // 高精度定位模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //  设置单次定位
        option.setOnceLocation(true);
        option.setNeedAddress(true);
        client.setLocationOption(option);
        client.startLocation();

    }

    @OnPermissionDenied(value = {Manifest.permission.ACCESS_FINE_LOCATION})
    void onLocationDenied() {
        showAskDialog("您拒绝了定位权限，是否现在去开启？");
    }

    @OnNeverAskAgain(value = {Manifest.permission.ACCESS_FINE_LOCATION})
    void onLocationNeverAsk() {
        showAskDialog("您禁止了定位权限，是否现在去开启？");
    }

    private void showAskDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    viewModel.setLaunchAppDetail(true);
                    AppUtils.launchAppDetailsSettings(getActivity().getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    //  用户拒绝了定位权限，先判断是否选择过城市，是，则取选择的城市，否，则将城市设为北京
                    LocationBean locationBean = new LocationBean(null, null);
                    viewModel.location.set(locationBean);
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private static class HomeNavigatorAdapter extends FragmentPagerAdapter {

        public HomeNavigatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return RecommandFragment.newInstance();
                case 1:
                    return NormalFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private static class ViewPager2Adapter extends FragmentStateAdapter {

        public ViewPager2Adapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RecommandFragment.newInstance();
                case 1:
                    return NormalFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}