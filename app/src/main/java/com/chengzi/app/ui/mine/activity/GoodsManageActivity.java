package com.chengzi.app.ui.mine.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityGoodsManageBindingImpl;
import com.chengzi.app.databinding.ItemGoodsManageListLayoutBinding;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.popu.DropDownGridPopup;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.mine.bean.GoodsListManageBean;
import com.chengzi.app.ui.mine.viewmodel.GoodsManangeViewModel;
import com.chengzi.app.ui.search.OnDisplayChangeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品管理
 *
 * @ClassName:GoodsManageActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/17 0017   15:53
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/17 0017 handongkeji All rights reserved.
 */

public class GoodsManageActivity extends BaseActivity<ActivityGoodsManageBindingImpl, GoodsManangeViewModel>
        implements OnDisplayChangeObserver, View.OnClickListener {

    private PagingLoadHelper helper;
    private DropDownGridPopup gridPopup;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_goods_manage;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setSelectedTabPos(mViewModel.selectedTabPos);


        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        onDisplayChange(mViewModel.display.get());

        mViewModel.goodsListLive.observe(this, objects -> {
            dismissLoading();
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        //设置/取消特色
        mViewModel.goodsChangeFeatureLive.observe(this, bean -> {
            dismissLoading();
            if (bean.getStatus() == Sys.SUCCESS_STATUS) {
                helper.onPulldown();
            }
        });
        //上/下架
        mViewModel.goodsChangeStatusLive.observe(this, bean -> {
            dismissLoading();
            if (bean.getStatus() == Sys.SUCCESS_STATUS) {
                helper.onPulldown();
            }
        });

        //获取全部分类(默认选中 全部)
        mViewModel.goodsCategory();
        mViewModel.goodsCategoryLiveData.observe(this, list -> {

            if (gridPopup != null) {
                gridPopup.setData(list);
            }

        });
    }

    private String choose_sort = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all_goods: //  所有商品
                mBinding.tvSort.setText("筛选");
                mViewModel.selectedTabPos.set(0);
                mViewModel.status = null;

                //所有一级分类
                if (gridPopup == null) {
                    gridPopup = new DropDownGridPopup(this);
                }
                gridPopup.showAsDropDown(v);

                if (mViewModel.goodsCategoryLiveData.getValue() != null) {
                    gridPopup.setData(mViewModel.goodsCategoryLiveData.getValue());
                }

                gridPopup.setOnDismissListener(() -> {
                    mViewModel.category_id = null;
                });
                gridPopup.setSureListener(bean -> {

                    String categoryId = bean.getId();
                    String category_name = bean.getName();
                    mBinding.tvAllGoods.setText(category_name);
                    mViewModel.category_id = categoryId;

                    showLoading(Sys.LOADING);
                    helper.onPulldown();
                });
                break;
            case R.id.layout_sort:   //  筛选
                mBinding.tvAllGoods.setText(getString(R.string.all_goods_type));
                mViewModel.selectedTabPos.set(1);
                new SelectListDialog(this, getSort(), choose_sort)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                if (causeid != 0) {
                                    choose_sort = cause;
                                    mViewModel.status = String.valueOf(causeid);
                                    mViewModel.category_id = null;
                                    mBinding.tvSort.setText(cause);
                                    showLoading(Sys.LOADING);
                                    helper.onPulldown();
                                } else {//全部
                                    mViewModel.selectedTabPos.set(-1);
                                    mViewModel.status = null;
                                    mViewModel.category_id = null;
                                    showLoading(Sys.LOADING);
                                    helper.onPulldown();
                                }
                            }
                        })
                        .setTvCancle("取消")
                        .show();
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) {
//            return;
//        }
//        switch (requestCode) {
//            case REQUEST_CODE_SELECT_GOODS:
//                Bundle bundle = data.getExtras();
//                String first_id = bundle.getString("first_id");
//                String second_id = bundle.getString("second_id");
//                String third_id = bundle.getString("third_id");
//                mViewModel.status = null;
//                mViewModel.category_id = first_id;
//                mViewModel.category_id2 = second_id;
//                mViewModel.category_id3 = third_id;
//                showLoading(Sys.LOADING);
//                helper.onPulldown();
//                break;
//        }
//    }

    private List<SelectListBean> getSort() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(0, "全部"));
        strings.add(new SelectListBean(1, "上架中"));
        strings.add(new SelectListBean(2, "下架中"));
        strings.add(new SelectListBean(3, "特色商品"));
        return strings;
    }

    @Override
    public void onDisplayChange(Display display) {
        mViewModel.display.set(display);

        List<GoodsListManageBean> originData = null;
        BaseQuickAdapter originAdapter = (BaseQuickAdapter) mBinding.swipeRefreshView.getRecyclerView().getAdapter();
        if (originAdapter != null && originAdapter.getData() != null && !originAdapter.getData().isEmpty()) {
            originData = originAdapter.getData();
        }

        if (display == Display.LIST) {
            mBinding.swipeRefreshView.setLayoutManager(new LinearLayoutManager(this));
            GoodsManageListAdapter adapter = new GoodsManageListAdapter((view, bean) -> {
                GoodsListManageBean goodsListManageBean = (GoodsListManageBean) bean;
                switch (view.getId()) {
                    case R.id.tv_set_special_goods:     //设为特色商品
                    case R.id.tv_cancel_special_goods:     //取消特色商品
                        showLoading(Sys.LOADING);
                        mViewModel.goodsChangeFeature(goodsListManageBean.getId());
                        break;
                    case R.id.tv_up_shelves:     //上架
                    case R.id.tv_down_shelves:     //下架
                        showLoading(Sys.LOADING);
                        mViewModel.goodsChangeStatus(goodsListManageBean.getId());
                        break;
                    case R.id.ll_goods:     //点击商品到商品详情
                        if (TextUtils.equals(goodsListManageBean.getStatus(), "1")) {
                            GoodsDetailActivity.start(this, goodsListManageBean.getId());
                        } else {
                            toast("该商品已下架");
                        }
                        break;
                }
            });
            mBinding.swipeRefreshView.setAdapter(adapter);
        }

        if (originAdapter == null || originData == null) {
            helper.start();
        } else {
            helper.onComplete(originData);
        }
    }

    public class GoodsManageListAdapter extends BaseQuickAdapter<GoodsListManageBean, BaseViewHolder> {

        private ClickEventHandler clickEventHandler;

        public GoodsManageListAdapter(ClickEventHandler clickEventHandler) {
            super(R.layout.item_goods_manage_list_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, GoodsListManageBean item) {
            ItemGoodsManageListLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setListener(clickEventHandler);
            layoutBinding.setUrl(item.getImage());
            //是否是VIP ->is_VIP// 是否是vip   0 否 1是',
            String is_vip = item.getIs_VIP();
            if (TextUtils.equals(is_vip, "1")) {
                layoutBinding.ivIsVip.setVisibility(View.VISIBLE);
            } else {
                layoutBinding.ivIsVip.setVisibility(View.GONE);
            }
            // feature; 是否是特色商品，1-非特色  2-特色
            String feature = item.getFeature();
            layoutBinding.tvSetSpecialGoods.setVisibility(TextUtils.equals(feature, "2") ? View.GONE : View.VISIBLE);
            layoutBinding.tvCancelSpecialGoods.setVisibility(TextUtils.equals(feature, "2") ? View.VISIBLE : View.GONE);
            // status; 商品状态 1-上架 2-下架
            String status = item.getStatus();
            layoutBinding.tvUpShelves.setVisibility(TextUtils.equals(status, "1") ? View.GONE : View.VISIBLE);
            layoutBinding.tvDownShelves.setVisibility(TextUtils.equals(status, "1") ? View.VISIBLE : View.GONE);
            layoutBinding.executePendingBindings();
        }
    }
}