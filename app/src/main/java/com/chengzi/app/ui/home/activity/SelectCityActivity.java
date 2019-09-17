package com.chengzi.app.ui.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivitySelectCityBinding;
import com.chengzi.app.databinding.ItemCityLayoutBinding;
import com.chengzi.app.databinding.ItemProvinceLayoutBinding;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.common.bean.ProvinceBean;
import com.chengzi.app.ui.home.bean.LocationBean;
import com.chengzi.app.ui.home.viewmodel.SelectCityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页--选择城市
 *
 * @ClassName:SelectCityActivity
 * @PackageName:com.yimei.app.ui.home.activity
 * @Create On 2019/4/3 0003   16:05
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class SelectCityActivity extends BaseActivity<ActivitySelectCityBinding, SelectCityViewModel> {

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_select_city;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        initProvinceRecycler();
        initCityRecycler();

        observe();
        mViewModel.getCities();
    }

    private void parseIntent() {
        LocationBean locationBean = (LocationBean) getIntent().getSerializableExtra("location");
        if (locationBean != null) {
            String cityName = locationBean.getCityName();
            mBinding.currentCity.setVisibility(TextUtils.isEmpty(cityName) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(cityName)) {
                mBinding.currentCity.setText(cityName);
            }
        } else {
            mBinding.currentCity.setVisibility(View.GONE);
        }
    }

    private void observe() {
        mViewModel.liveData.observe(this, provinceBeans -> {
            mBinding.viewSwitcher.setDisplayedChild(1);
            provinceAdapter.setNewData(provinceBeans);
            if (provinceBeans != null && !provinceBeans.isEmpty()) {
                List<CityBean> list = provinceBeans.get(0).getChild();
                cityAdapter.setNewData(new ArrayList<>(list));
            }
        });
    }

    private void initProvinceRecycler() {
        provinceAdapter = new ProvinceAdapter();
        provinceAdapter.setHasStableIds(true);
        mBinding.recyclerProvince.setAdapter(provinceAdapter);
        provinceAdapter.setOnItemClickListener((adapter, view, position) -> {
            mViewModel.selectedProvincePos.set(position);
            ProvinceBean item = provinceAdapter.getItem(position);
            int size = cityAdapter.getData().size();
            int size1 = item.getChild().size();

            cityAdapter.getData().clear();
            cityAdapter.notifyItemRangeRemoved(cityAdapter.getHeaderLayoutCount(), size);
            cityAdapter.getData().addAll(item.getChild());
            cityAdapter.notifyItemRangeInserted(cityAdapter.getHeaderLayoutCount(), size1);

        });

    }

    private void initCityRecycler() {
        cityAdapter = new CityAdapter();
        cityAdapter.setHasStableIds(true);
        mBinding.recyclerCity.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener((adapter, view, position) -> {
            CityBean item = cityAdapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("city", item);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

    }

    private class ProvinceAdapter extends BaseQuickAdapter<ProvinceBean, BaseViewHolder> {

        public ProvinceAdapter() {
            super(R.layout.item_province_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ProvinceBean item) {
            ItemProvinceLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setPos(helper.getAdapterPosition());
            layoutBinding.setSelectedPos(mViewModel.selectedProvincePos);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

    private class CityAdapter extends BaseQuickAdapter<CityBean, BaseViewHolder> {

        public CityAdapter() {
            super(R.layout.item_city_layout);
        }


        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return super.onCreateViewHolder(parent, viewType);
        }


        @Override
        protected void convert(BaseViewHolder helper, CityBean item) {
            ItemCityLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

}
