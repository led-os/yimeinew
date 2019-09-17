package com.chengzi.app.ui.home.fragment;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.adapter.RecommandAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentRecommandBinding;
import com.chengzi.app.databinding.ItemRecommandHeaderLayoutBinding;
import com.chengzi.app.databinding.ItemRecommandThirdpartLayoutBinding;
import com.chengzi.app.databinding.ItemThirdPartLayoutBinding;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.event.OnLoginSuccessEvent;
import com.chengzi.app.ui.common.activity.WebActivity;
import com.chengzi.app.ui.home.bean.AdvertisingBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.home.viewmodel.HomeViewModel;
import com.chengzi.app.ui.home.viewmodel.RecommandViewModel;
import com.chengzi.app.ui.peopleraise.activity.PeopleRaiseActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferTypeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐频道
 *
 * @ClassName:RecommandFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   16:20
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class RecommandFragment extends BaseFragment<FragmentRecommandBinding, RecommandViewModel>
        implements View.OnClickListener {


    private ThirdPartAdapter thirdPartAdapter;
    private RecommandAdapter recommandAdapter;

    public static RecommandFragment newInstance() {
        Bundle args = new Bundle();
        RecommandFragment fragment = new RecommandFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecommandFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_recommand;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        MainDynamicFragment fragment = (MainDynamicFragment) getChildFragmentManager().findFragmentByTag(MainDynamicFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onHiddenChanged(hidden);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() == null) {
            return;
        }
        MainDynamicFragment fragment = (MainDynamicFragment) getChildFragmentManager().findFragmentByTag(MainDynamicFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onHiddenChanged(!isVisibleToUser);
        }
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

        initRecyclerView();
        observe();

    }

    // 刷新数据
    public void refresh() {
        viewModel.homeRecommand();
        viewModel.likeGoodsList();
        viewModel.refreshLive.postValue(true);
    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event) {
        refresh();
    }

    //  登录成功后，会根据用户的历史搜索记录，给用户推荐他可能喜欢的商品
    @Subscribe
    public void onLoginSuccess(OnLoginSuccessEvent event) {
        viewModel.likeGoodsList();
    }

    private void observe() {
        viewModel.homeRecommandLive.observe(this, homeRecommandBeans -> {

            HomeRecommandBean header = null;
            if (!viewModel.items.isEmpty() && viewModel.items.get(0).getType() == 1) {
                header = viewModel.items.get(0);
            }

            viewModel.items.clear();

            for (HomeRecommandBean homeRecommandBean : homeRecommandBeans) {
                List<GoodBean> lists = homeRecommandBean.getGoodsLists();
                if (lists != null && !lists.isEmpty()) {
                    viewModel.items.add(homeRecommandBean);
                }
            }
            if (header != null) {
                viewModel.items.add(0, header);
            }
            recommandAdapter.replaceData(viewModel.items);
        });

        viewModel.likeGoodsLive.observe(this, goodBeans -> {
            if (goodBeans == null) {
                goodBeans = new ArrayList<>();
            }

            if (!viewModel.items.isEmpty() && viewModel.items.get(0).getType() == 1) {
                HomeRecommandBean homeRecommandBean = viewModel.items.get(0);
                homeRecommandBean.setGoodsLists(goodBeans);
                if (homeRecommandBean.getGoodsLists().isEmpty()) {
                    viewModel.items.remove(0);
                }
            } else {
                if (!goodBeans.isEmpty()) {
                    HomeRecommandBean recommandBean = new HomeRecommandBean();
                    recommandBean.setType(1);
                    recommandBean.setGoodsLists(goodBeans);
                    recommandBean.setName("猜你喜欢");
                    viewModel.items.add(0, recommandBean);
                }
            }

            recommandAdapter.replaceData(viewModel.items);

        });

        HomeViewModel homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        homeViewModel.cityAndCategoryInitLive.observe(this, aBoolean -> {

            if (homeViewModel.cityAndCategoryStatus[0]) {
                viewModel.homeRecommand();
                viewModel.likeGoodsList();
            }
        });

    }

    private void initRecyclerView() {

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);
        recommandAdapter = new RecommandAdapter();

        recommandAdapter.addHeaderView(createHeader());
        recommandAdapter.addFooterView(thirtPartView());

        recommandAdapter.setFactory(new RecommandAdapter.ItemAdapterFactory() {
            @Override
            public BaseQuickAdapter createAdapter(List<GoodBean> list) {
                MainGoodsAdapter adapter = new MainGoodsAdapter(1);
                adapter.setNewData(list);
                return adapter;
            }

            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });
        binding.recyclerRecommand.setAdapter(recommandAdapter);
    }

    private View createHeader() {
        ItemRecommandHeaderLayoutBinding headerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.item_recommand_header_layout, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = SizeUtils.dp2px(15);
        headerBinding.getRoot().setLayoutParams(params);
        headerBinding.setListener(this);
        headerBinding.mainTimeUpContainer.post(new Runnable() {

            @Override
            public void run() {
                if (getView() == null || getView().findViewById(R.id.main_time_up_container) == null) {
                    headerBinding.mainDynamicContainer.post(this);
                } else {
                    addMainTimeup();
                }
            }
        });
        headerBinding.mainDynamicContainer.post(new Runnable() {

            @Override
            public void run() {
                if (getView() == null || getView().findViewById(R.id.main_dynamic_container) == null) {
                    headerBinding.mainDynamicContainer.post(this);
                } else {
                    addMainDynamic();
                }
            }
        });
        return headerBinding.getRoot();
    }

    private View thirtPartView() {

        ItemRecommandThirdpartLayoutBinding thirdpartBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.item_recommand_thirdpart_layout, binding.recyclerRecommand, false);

        thirdPartAdapter = new ThirdPartAdapter();
        thirdpartBinding.thirdPartyRecycler.setAdapter(thirdPartAdapter);
        thirdPartAdapter.setOnItemClickListener((adapter, view, position) -> {
            AdvertisingBean bean = thirdPartAdapter.getItem(position);
            WebActivity.start(getActivity(),bean.getUrl());
        });

        return thirdpartBinding.getRoot();
    }

    //  添加秒杀视图
    private void addMainTimeup() {
        MutableLiveData liveData = new MutableLiveData();
        liveData.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                liveData.removeObserver(this);
                Fragment fragment = MainTimeUpFragment.newInstance();

                if (!fragment.isAdded()) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.main_time_up_container, fragment, MainTimeUpFragment.class.getSimpleName())
                            .commit();
                }
            }
        });
        liveData.postValue(1);
    }

    //  添加动态视图
    private void addMainDynamic() {
        MutableLiveData liveData = new MutableLiveData();
        liveData.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                liveData.removeObserver(this);
                Fragment fragment = MainDynamicFragment.newInstance();
                if (!fragment.isAdded()) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.main_dynamic_container, fragment, MainDynamicFragment.class.getSimpleName())
                            .commit();
                }
            }
        });
        liveData.postValue(1);
    }

    public void setUpThirdpart(List<AdvertisingBean> list) {
        thirdPartAdapter.setNewData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refer:
            case R.id.btn_private_refer: //  私享咨询
                PrivateReferActivity.start(getActivity());
                break;
            case R.id.btn_diagnose_online:  //  在线面诊
                PrivateReferTypeActivity.start(getActivity(), Sys.TYPE_DIAGNOSE_ONLINE);
                break;
            case R.id.btn_people_raise: //  美人筹
                startActivity(new Intent(getActivity(), PeopleRaiseActivity.class));
                break;
        }
    }

    public static class ThirdPartAdapter extends BaseQuickAdapter<AdvertisingBean, BaseViewHolder> {

        public ThirdPartAdapter() {
            super(R.layout.item_third_part_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, AdvertisingBean item) {
            ItemThirdPartLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setUrl(item.getImage());
            layoutBinding.executePendingBindings();
        }

    }


}
