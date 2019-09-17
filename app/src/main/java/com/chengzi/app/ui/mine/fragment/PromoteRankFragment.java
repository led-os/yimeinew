package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentPromoteRankBindingImpl;
import com.chengzi.app.databinding.ItemPromoteRankLayoutBinding;
import com.chengzi.app.ui.mine.bean.ExtensionRankBean;
import com.chengzi.app.ui.mine.viewmodel.PromoteRankViewModel;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

/**
 * 推广排行
 *
 * @ClassName:IWantRecommendFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/15 0015   14:55
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class PromoteRankFragment extends BaseFragment<FragmentPromoteRankBindingImpl, PromoteRankViewModel> {


    private static final int REQUEST_CODE_SELECT_CITY = 0x01;
    //城市id
    private String city_id;

    public PromoteRankFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_promote_rank;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
//        binding.setListener(clickListener);
        binding.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        PromoteRankAdapter adapter = new PromoteRankAdapter();
        binding.mRecyclerView.setAdapter(adapter);
        city_id = PreferenceManager.getPreference(PreferenceManager.CITY_ID);
        String city_name = PreferenceManager.getPreference(PreferenceManager.CITY_NAME);
        binding.tvCity.setText(city_name);

        showLoading(Sys.LOADING);
        //咨询师排行
        viewModel.extensionRank(city_id);
        viewModel.extensionRankLiveData.observe(this, extensionRankBean -> {
            dismissLoading();
            if (extensionRankBean != null && extensionRankBean.getStatus() == Sys.SUCCESS_STATUS && extensionRankBean.getData() != null
                    && extensionRankBean.getData().size() > 0) {
                binding.mRecyclerView.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);
                List<ExtensionRankBean> data = extensionRankBean.getData();
                adapter.setNewData(data);
            } else {
                binding.mRecyclerView.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

//    private ClickEventHandler<Object> clickListener = (view, bean) -> {
//        switch (view.getId()) {
//            case R.id.tv_city:  //  定位城市
//                LocationBean locationBean = new LocationBean("", PreferenceManager.getPreference(PreferenceManager.CITY_NAME));
//                startActivityForResult(new Intent(getActivity(), SelectCityActivity.class)
//                                .putExtra("location", locationBean),
//                        REQUEST_CODE_SELECT_CITY, data -> {
//                            CityBean cityBean = (CityBean) data.getSerializableExtra("city");
//                            city_id = cityBean.getCity_id();
//                            binding.tvCity.setText(cityBean.getCity_name());
//                            showLoading(Sys.LOADING);
//                            //咨询师排行
//                            viewModel.extensionRank(city_id);
//                        });
//                break;
//        }
//    };

    public class PromoteRankAdapter extends BaseQuickAdapter<ExtensionRankBean, BaseViewHolder> {
        public PromoteRankAdapter() {
            super(R.layout.item_promote_rank_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ExtensionRankBean item) {
            ItemPromoteRankLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
            binding.tvPosition.setText(String.valueOf(helper.getPosition() + 1));
        }
    }
}