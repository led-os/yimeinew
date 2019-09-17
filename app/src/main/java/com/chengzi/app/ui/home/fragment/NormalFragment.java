package com.chengzi.app.ui.home.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindOrgAdapter;
import com.chengzi.app.adapter.MainDoctorAdapter;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.databinding.FragmentNormalBinding;
import com.chengzi.app.databinding.ItemMainNormalLayoutBinding;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.home.viewmodel.NormalViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 普通频道
 *
 * @ClassName:NormalFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   16:19
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class NormalFragment extends BaseFragment<FragmentNormalBinding, NormalViewModel> {

    private NormalAdapter normalAdapter;

    public static NormalFragment newInstance() {
        Bundle args = new Bundle();
        NormalFragment fragment = new NormalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NormalFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_normal;
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
        observe();

    }

    private void observe() {
        viewModel.normalLive.observe(this, normalBean -> {
            List<HomeRecommandBean> data = normalAdapter.getData();
            normalAdapter.getData().clear();
            normalAdapter.notifyItemRangeRemoved(normalAdapter.getHeaderLayoutCount(), data.size());

            if (normalBean.getGoodsList() != null && !normalBean.getGoodsList().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean(viewModel.getCategoryName() + "专区", normalBean.getGoodsList(), 0));
            }
            if (normalBean.getDoctorList() != null && !normalBean.getDoctorList().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean(viewModel.getCategoryName() + "医生", normalBean.getDoctorList(), 1));
            }
            if (normalBean.getComList() != null && !normalBean.getComList().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean(viewModel.getCategoryName() + "咨询师", normalBean.getComList(), 2));
            }
            if (normalBean.getHostGoodsList() != null && !normalBean.getHostGoodsList().isEmpty()) {
                normalAdapter.addData(new HomeRecommandBean("热门商品", normalBean.getHostGoodsList(), 3));
            }
            if (normalBean.getHospitalList() != null && !normalBean.getHospitalList().isEmpty()) {
                for (HospitalBean hospitalBean : normalBean.getHospitalList()) {
                    hospitalBean.setIs_Vip(0);
                }
                normalAdapter.addData(new HomeRecommandBean(viewModel.getCategoryName() + "机构", normalBean.getHospitalList(), 4));
            }
            if (normalAdapter.getData().isEmpty()) {
                normalAdapter.notifyDataSetChanged();
                normalAdapter.setEmptyView(R.layout.empty_layout);
            }
        });
    }

    private void initRecyclerView() {
        normalAdapter = new NormalAdapter();
        normalAdapter.bindToRecyclerView(binding.recyclerViewNormal);
    }

    public void show(CategoryItem categoryItem) {
        viewModel.setCategoryId(categoryItem.getId());
        viewModel.setCategoryName(categoryItem.getName());
        viewModel.specialList();
    }

    public void refresh(){
        viewModel.specialList();
    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event){
        refresh();
    }

    class NormalAdapter extends BaseQuickAdapter<HomeRecommandBean, BaseViewHolder> {

        public NormalAdapter() {
            super(R.layout.item_main_normal_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeRecommandBean item) {
            ItemMainNormalLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);

            layoutBinding.getRoot().setBackgroundResource(R.drawable.shadow_gray_bg);
            layoutBinding.tvChannelName.setBackgroundResource(R.drawable.left_red_gray_square_bg);
            layoutBinding.tvChannelName.setText(item.getName());

            BaseQuickAdapter adapter = null;
            switch (getItemViewType(helper.getAdapterPosition())) {
                case 0:  //  推荐商品
                    adapter = new MainGoodsAdapter(1);
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 1:  //  专区医生
                    adapter = new MainDoctorAdapter(2);
                    adapter.replaceData(item.getGoodsLists());
                    layoutBinding.recycler.swapAdapter(adapter, false);
                    break;
                case 2:  //  专区咨询师
                    adapter = new MainDoctorAdapter(3);
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
                    layoutBinding.tvChannelName.setBackgroundResource(R.drawable.left_red_white_square_bg);
                    GridLayoutManager layoutManager = (GridLayoutManager) layoutBinding.recycler.getLayoutManager();
                    layoutManager.setSpanCount(4);
                    adapter = new FindOrgAdapter();
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
