package com.chengzi.app.ui.vip.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.adapter.VipDoctorAdapter;
import com.chengzi.app.adapter.VipOrgAdapter;
import com.chengzi.app.databinding.FragmentNormalVipBinding;
import com.chengzi.app.databinding.ItemMainNormalLayoutBinding;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.vip.viewmodel.VipViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 普通vip
 *
 * @ClassName:NormalVipFragment
 * @PackageName:com.yimei.app.ui.vip.fragment
 * @Create On 2019/4/13 0013   14:56
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class NormalVipFragment extends BaseFragment<FragmentNormalVipBinding, VipViewModel> {

    private NormalAdapter normalAdapter;

    public NormalVipFragment() {
    }

    public static NormalVipFragment newInstance() {
        android.os.Bundle args = new Bundle();
        NormalVipFragment fragment = new NormalVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_normal_vip;
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

        initRecyclerView();
        listenRefresh();
        observe();
    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event) {
        viewModel.getVipNormalChannel();
    }

    //  下拉刷新
    private void listenRefresh() {
//        viewModel.alwaysAliveObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                if (getUserVisibleHint()) {
//                    viewModel.getVipNormalChannel();
//                }
//            }
//        });
    }

    private void observe() {
        viewModel.currentCategory.observe(this, categoryItem -> {
            if (!getUserVisibleHint()) {
                return;
            }
            viewModel.getVipNormalChannel();
        });

        viewModel.vipNormalChannelLive.observe(this, normalBean -> {

            viewModel.refreshObservable.update();

            String categoryName = "";
            CategoryItem categoryItem = viewModel.currentCategory.getValue();
            if (categoryItem != null) {
                categoryName = categoryItem.getName();
            }

            List<HomeRecommandBean> data = normalAdapter.getData();
            normalAdapter.getData().clear();
            normalAdapter.notifyItemRangeRemoved(normalAdapter.getHeaderLayoutCount(), data.size());

            if (normalBean.getPromotion_goods_list() != null && !normalBean.getPromotion_goods_list().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("VIP" + categoryName + "专区", normalBean.getPromotion_goods_list(), 0));
            }
            if (normalBean.getDoctor_list() != null && !normalBean.getDoctor_list().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("VIP" + categoryName + "医生", normalBean.getDoctor_list(), 1));
            }
            if (normalBean.getConsultant_list() != null && !normalBean.getConsultant_list().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("VIP" + categoryName + "咨询师", normalBean.getConsultant_list(), 2));
            }
            if (normalBean.getGoods_list() != null && !normalBean.getGoods_list().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("VIP热门商品", normalBean.getGoods_list(), 3));
            }
            if (normalBean.getHospital_list() != null && !normalBean.getHospital_list().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("VIP" + categoryName + "机构", normalBean.getHospital_list(), 4));
            }

            if (normalAdapter.getData().isEmpty()) {
                normalAdapter.notifyDataSetChanged();
                View empty = LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null, false);

                normalAdapter.setEmptyView(empty);
            }
        });
    }

    private void initRecyclerView() {
        normalAdapter = new NormalAdapter();
        binding.recyclerView.setAdapter(normalAdapter);
    }

    class NormalAdapter extends BaseQuickAdapter<HomeRecommandBean, BaseViewHolder> {

        public NormalAdapter() {
            super(R.layout.item_main_normal_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeRecommandBean item) {
            ItemMainNormalLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);

            layoutBinding.getRoot().setBackgroundResource(R.drawable.shadow_gray_bg);
            layoutBinding.tvChannelName.setBackgroundResource(R.drawable.left_gold_gray_square_bg);
            layoutBinding.tvChannelName.setText(item.getName());

            int itemViewType = getItemViewType(helper.getAdapterPosition());

            BaseQuickAdapter adapter = null;
            switch (itemViewType) {
                case 0:  //  推荐商品
                    adapter = new MainGoodsAdapter(1);
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 1:  //  专区医生
                    adapter = new VipDoctorAdapter();
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 2:  //  专区咨询师
                    adapter = new VipDoctorAdapter();
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 3:  //  热门商品
                    adapter = new MainGoodsAdapter();
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 4:  //  专区机构
                    layoutBinding.getRoot().setBackground(null);
                    layoutBinding.tvChannelName.setBackgroundResource(R.drawable.left_gold_white_square_bg);
                    GridLayoutManager layoutManager = (GridLayoutManager) layoutBinding.recycler.getLayoutManager();
                    layoutManager.setSpanCount(4);
                    adapter = new VipOrgAdapter();
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
            }
        }

        @Override
        protected int getDefItemViewType(int position) {
            return getItem(position).getType();
        }

    }

}
