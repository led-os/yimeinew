package com.chengzi.app.ui.mine.activity.editinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.netease.nim.uikit.common.ui.recyclerview.decoration.DividerItemDecoration;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHospitalAddressBinding;
import com.chengzi.app.databinding.ItemPoiLayoutBinding;
import com.chengzi.app.ui.mine.viewmodel.HospitalAddressViewModel;

import java.util.ArrayList;

/**
 * 机构地址
 *
 * @ClassName:HospitalAddressActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/19 0019   11:37
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class HospitalAddressActivity extends BaseActivity<ActivityHospitalAddressBinding,
        HospitalAddressViewModel> implements PoiSearch.OnPoiSearchListener {

    public static final String EXTRA_CITY_NAME = "extra_city_name";
    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    //所属城市 地址
    private String cityName, address;
    private double latitude, longitude;
    private PoiAdapter poiAdapter;
    private PoiItem item;

    public static void startForResult(Context context, String cityName, String address, double latitude,
                                      double longitude, int requestCode) {
        Intent intent = new Intent(context, HospitalAddressActivity.class);
        intent.putExtra(EXTRA_CITY_NAME, cityName)
                .putExtra(EXTRA_ADDRESS, address);
        if (latitude > 0) {
            intent.putExtra(EXTRA_LATITUDE, latitude);
        }
        if (longitude > 0) {
            intent.putExtra(EXTRA_LONGITUDE, longitude);
        }

        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_address;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setListener(clickListener);
        mBinding.setViewModel(mViewModel);

        mBinding.tvCityName.setText(cityName);
        mBinding.etAddress.setText(address);

        //保存
        mBinding.topBar.setOnRightClickListener(v -> {
            if (TextUtils.isEmpty(mViewModel.detailAddress.get())) {
                ToastUtils.showShort("请填写详细地址");
                return;
            }

            if (poiAdapter.selectedPos.get() < 0) {
                ToastUtils.showShort("请在地图上选择具体地址");
                return;
            }

            item = poiAdapter.getItem(poiAdapter.selectedPos.get());


//            Intent intent = new Intent();
//            intent.putExtra("city_name", cityName);
//            intent.putExtra("address", mViewModel.detailAddress.get())
//                    .putExtra("latitude", item.getLatLonPoint().getLatitude())
//                    .putExtra("longitude", item.getLatLonPoint().getLongitude());
//            setResult(200, intent);
//            finish();

            Params params = Params.newParams()
                    .put("hospital_address", mViewModel.detailAddress.get())
                    .put("latitude", item.getLatLonPoint().getLatitude() + "")
                    .put("longitude", item.getLatLonPoint().getLongitude() + "");
            mViewModel.updateInfo(params.params());
        });
        //修改地址成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                double latitude = item.getLatLonPoint().getLatitude();
                double longitude = item.getLatLonPoint().getLongitude();
                Intent intent = new Intent();
                intent.putExtra("address", mViewModel.detailAddress.get())
                        .putExtra("latitude", latitude)
                        .putExtra("longitude", longitude);
                //修改本地保存的地址
                AccountHelper.setCity(String.valueOf(latitude), String.valueOf(longitude));
                setResult(200, intent);
                finish();
            }
        });

        init();

        // 创建地图
        mBinding.mapView.onCreate(savedInstanceState);

        poiAdapter = new PoiAdapter();
        mBinding.recyclerView.setAdapter(poiAdapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        poiAdapter.setOnItemClickListener((adapter, view, position) -> {
            PoiItem item = poiAdapter.getItem(position);
            LatLonPoint point = item.getLatLonPoint();
            CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(new LatLng(point.getLatitude(), point.getLongitude()));
            aMap.moveCamera(cameraUpdate);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            poiAdapter.selectedPos.set(position);
        });

        mBinding.etSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(mViewModel.keyword.get())) {
                    toast("请输入搜索关键词");
                    return false;
                }
                PoiSearch.Query query = new PoiSearch.Query(mViewModel.keyword.get(), "", cityName);
                query.setCityLimit(true);
                query.setPageNum(0);
                query.setPageSize(20);
                PoiSearch poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
                return true;
            }
            return false;
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        cityName = intent.getStringExtra(EXTRA_CITY_NAME);
        address = intent.getStringExtra(EXTRA_ADDRESS);
        latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 39.904989f);   //  默认北京
        longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 116.405285f);    //  默认北京

    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_search:      //搜索
                if (TextUtils.isEmpty(mViewModel.keyword.get())) {
                    toast("请输入搜索关键词");
                    return;
                }
                PoiSearch.Query query = new PoiSearch.Query(mViewModel.keyword.get(), "", cityName);
                query.setCityLimit(true);
                query.setPageNum(0);
                query.setPageSize(20);
                PoiSearch poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
                break;
        }
    };

    private void init() {

        if (aMap == null) {
            // 显示地图
            aMap = mBinding.mapView.getMap();
        }

        aMap.getUiSettings().setZoomControlsEnabled(false);
        // 设置地图默认的指南针是否显示
        aMap.getUiSettings().setCompassEnabled(false);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //表示设置定位图标为自定义图标
        //普通地图
        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);

        // 设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        // 将地图移动到定位点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图
        mBinding.mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i != 1000) {
            ToastUtils.showShort("搜索失败");
            return;
        }
        ArrayList<PoiItem> pois = poiResult.getPois();
        if (pois == null || pois.isEmpty()) {
            ToastUtils.showShort("未搜索到相关地址");
            return;
        }
        poiAdapter.setNewData(pois);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    public static class PoiAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

        public final ObservableInt selectedPos = new ObservableInt(-1);

        public PoiAdapter() {
            super(R.layout.item_poi_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, PoiItem item) {
            ItemPoiLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setPos(helper.getAdapterPosition());
            layoutBinding.setSelectedPos(selectedPos);
            layoutBinding.textView.setText(item.getTitle());
            layoutBinding.executePendingBindings();
        }

    }

}
