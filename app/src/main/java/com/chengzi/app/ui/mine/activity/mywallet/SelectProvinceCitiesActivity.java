package com.chengzi.app.ui.mine.activity.mywallet;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivitySelectProvinceCitiesBindingImpl;
import com.chengzi.app.databinding.ItemProcinceCityLayout1Binding;
import com.chengzi.app.databinding.ItemProcinceCityLayoutBinding;
import com.chengzi.app.ui.mine.bean.BankCityBean;
import com.chengzi.app.ui.mine.viewmodel.SelectProvinceCitiesViewModel;

/**
 * 对公账户选择城市
 *
 * @ClassName:SelectProvinceCitiesActivity
 * @PackageName:com.yimei.app.ui.mine.activity.mywallet
 * @Create On 2019/6/19 0019   15:19
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/6/19 0019 handongkeji All rights reserved.
 */

public class SelectProvinceCitiesActivity extends BaseActivity<ActivitySelectProvinceCitiesBindingImpl, SelectProvinceCitiesViewModel> {

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private String province_id, province_name;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_select_province_cities;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initProvinceRecycler();
        initCityRecycler();

        mViewModel.bankCity("0");
        observe();

    }

    private void observe() {
        mViewModel.bankProvinceLiveData.observe(this, provinceBeans -> {
            if (provinceBeans != null && provinceBeans.size() > 0) {
//                mBinding.viewSwitcher.setDisplayedChild(1);
                provinceAdapter.setNewData(provinceBeans);
                BankCityBean bankCityBean = provinceBeans.get(0);
                province_id = bankCityBean.getId();
                province_name = bankCityBean.getName();
                mViewModel.bankCity(bankCityBean.getId());
            } else {
                ///暂无数据

            }
        });
        mViewModel.bankCityLiveData.observe(this, provinceBeans -> {
            if (provinceBeans != null && provinceBeans.size() > 0) {
                cityAdapter.setNewData(provinceBeans);
                mViewModel.bankCity(provinceBeans.get(0).getId());
            } else {
                ///暂无数据

            }
        });
    }

    private void initProvinceRecycler() {
        provinceAdapter = new ProvinceAdapter();
//        provinceAdapter.setHasStableIds(true);
        mBinding.recyclerProvince.setAdapter(provinceAdapter);
        provinceAdapter.setOnItemClickListener((adapter, view, position) -> {
            mViewModel.selectedProvincePos.set(position);
            BankCityBean item = provinceAdapter.getItem(position);

            province_id = item.getId();
            province_name = item.getName();
            mViewModel.bankCity(item.getId());
        });
    }

    private void initCityRecycler() {
        cityAdapter = new CityAdapter();
//        cityAdapter.setHasStableIds(true);
        mBinding.recyclerCity.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener((adapter, view, position) -> {
            BankCityBean cityItem = cityAdapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("province_id", province_id);
            intent.putExtra("province_name", province_name);
            intent.putExtra("city_id", cityItem.getId());
            intent.putExtra("city_name", cityItem.getName());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    private class ProvinceAdapter extends BaseQuickAdapter<BankCityBean, BaseViewHolder> {

        public ProvinceAdapter() {
            super(R.layout.item_procince_city_layout1);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankCityBean item) {
            ItemProcinceCityLayout1Binding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setPos(helper.getAdapterPosition());
            layoutBinding.setSelectedPos(mViewModel.selectedProvincePos);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

    private class CityAdapter extends BaseQuickAdapter<BankCityBean, BaseViewHolder> {

        public CityAdapter() {
            super(R.layout.item_procince_city_layout);
        }


        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return super.onCreateViewHolder(parent, viewType);
        }


        @Override
        protected void convert(BaseViewHolder helper, BankCityBean item) {
            ItemProcinceCityLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }
}
