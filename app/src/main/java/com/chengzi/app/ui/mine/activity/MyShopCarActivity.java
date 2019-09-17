package com.chengzi.app.ui.mine.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyShopCarBindingImpl;
import com.chengzi.app.databinding.ItemShopCarLayoutBinding;
import com.chengzi.app.event.OrderPaymentTypeEvent;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.mine.bean.ShopCarBean;
import com.chengzi.app.ui.mine.viewmodel.MyShopCarViewModel;
import com.chengzi.app.ui.pay.activity.PayOrderActivity;
import com.chengzi.app.utils.CustomLinearLayoutManager;
import com.chengzi.app.widget.NumberActionView;

import org.greenrobot.eventbus.EventBus;

/**
 * 购物车 -->只有普通用户
 *
 * @ClassName:MyShopCarActivity
 * @PackageName:com.yimei.app.ui.mine
 * @Create On 2019/4/4 0004   15:40
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class MyShopCarActivity extends BaseActivity<ActivityMyShopCarBindingImpl, MyShopCarViewModel> {


    //购物车列表adapter
    private ShopCarAdapter shopCarAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_shop_car;
    }

//    List<ShopCarBean> data = new ArrayList<>();

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(shorcarClickListener);
        CustomLinearLayoutManager customGridLayoutManager = new CustomLinearLayoutManager(this);
        customGridLayoutManager.setScrollEnabled(false);
        mBinding.mRecyclerView.setLayoutManager(customGridLayoutManager);

        //默认总计的价钱是0元
        mBinding.tvTotalMoney.setText("合计：¥0.00");
        shopCarAdapter = new ShopCarAdapter(shorcarAdapterClickListener);
        //解决闪烁问题
        shopCarAdapter.setHasStableIds(true);
        mBinding.mRecyclerView.setAdapter(shopCarAdapter);
//        ((SimpleItemAnimator) mBinding.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        //商品详情
        shopCarAdapter.setOnItemClickListener((adapter, view, position) ->
                GoodsDetailActivity.start(MyShopCarActivity.this, shopCarAdapter.getData().get(position).getGoods_id())
        );

        //0编辑结算 1完成删除
        mBinding.topBar.setOnRightClickListener(v -> {

            mViewModel.setType(mViewModel.getType() == 0 ? 1 : 0);

            mBinding.topBar.setRightText(mViewModel.getType() == 1 ? "完成" : "编辑");
            mBinding.tvSubmit.setText(mViewModel.getType() == 1 ? "删除" : "结算");
//            mBinding.ivShopcar.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
            mBinding.tvTotalMoney.setVisibility(mViewModel.getType() == 1 ? View.INVISIBLE : View.VISIBLE);
        });

        //回调处理数据
        observe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading(Sys.LOADING);
        mViewModel.onRequest(1, Integer.MAX_VALUE);
    }

    //处理数据——>列表 修改数量 获取总计 删除
    private void observe() {
        //购物车列表
        mViewModel.cartGoodsListLiveData.observe(this, objects -> {
            dismissLoading();

            boolean chooseAll = true;
            for (ShopCarBean object : objects) {
                if (mViewModel.selectedIds.contains(object.getCart_info_id())) {
                    object.setIsChoose(true);
                } else {
                    chooseAll = false;
                }
            }

            mViewModel.setChooseAll(chooseAll);
            mBinding.ivChooseAll.setSelected(chooseAll);

            if (mViewModel.selectedIds.isEmpty()) {
                mBinding.tvTotalMoney.setText("合计：¥0.00");
                mBinding.elDiscountCoupon.setContent("已抵扣0元");
            } else {
                setRecountMoney();
            }

            shopCarAdapter.setNewData(objects);
            if (shopCarAdapter.getData().isEmpty()) {
                //没有数据
                mBinding.llLayout.setVisibility(View.GONE);
                mBinding.emptyView.setVisibility(View.VISIBLE);
            }


        });

        //修改数量
        mViewModel.updateCartNumLiveData.observe(this, responseBean -> {
            mViewModel.aliveObservable.update();
            //重新计算优惠券和总计
            setRecountMoney();
        });
        //计算总计和优惠券价格
        mViewModel.cartGetPriceLiveData.observe(this, cartGetPriceBean -> {
            dismissLoading();
            if (cartGetPriceBean != null) {
                mBinding.elDiscountCoupon.setContent("已抵扣" + cartGetPriceBean.getDiscount_amount() + "元");
                mBinding.tvTotalMoney.setText("合计：¥" + cartGetPriceBean.getTotal_amount());
            }
        });

        //删除成功后 重新计算优惠券和总计
        mViewModel.cartDeleteLiveData.observe(this, responseBean -> {
//            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                //删除成功后 状态变成完成-->
                mViewModel.setType(0);
                mBinding.topBar.setRightText("编辑");
                mBinding.tvSubmit.setText("结算");
                //全选按钮 为未选中状态-->
                mBinding.ivChooseAll.setSelected(false);
                mViewModel.setChooseAll(false);
                mViewModel.selectedIds.clear();
                //刷新购物车
                mViewModel.onRequest(1, Integer.MAX_VALUE);
                mBinding.tvTotalMoney.setText("合计：¥0.00");
                mBinding.elDiscountCoupon.setContent("已抵扣0元");
//                mBinding.ivShopcar.setVisibility(View.GONE);
                mBinding.tvTotalMoney.setVisibility(View.VISIBLE);
            } else {
                dismissLoading();
            }
        });

        //购物车-->下单成功 ->返回的订单数组传到下个页面 显示支付信息
        mViewModel.cartAddOrderLiveData.observe(this, orderIds -> {
            dismissLoading();
            if (orderIds != null && orderIds.size() > 0) {
                mViewModel.selectedIds.clear();
                EventBus.getDefault().postSticky(new OrderPaymentTypeEvent(1));
                PayOrderActivity.start(this, 1, orderIds);
            }
        });
    }

    //重新计算优惠券和总计
    private void setRecountMoney() {
        if (!mViewModel.selectedIds.isEmpty()) {
            mViewModel.cartGetPrice(mViewModel.selectedIds);
        }
    }

    //页面 -> 全选/取消全选  结算/删除
    private ClickEventHandler<ShopCarBean> shorcarClickListener = (view, bean) -> {
        switch (view.getId()) {

            case R.id.iv_choose_all:  //  全选
            case R.id.tv_choose_all:
                //全选-->店铺和店铺中的商品全选

                mViewModel.setChooseAll(!mViewModel.isChooseAll());
                mBinding.ivChooseAll.setSelected(mViewModel.isChooseAll());

                mViewModel.selectedIds.clear();
                for (ShopCarBean carBean : shopCarAdapter.getData()) {
                    carBean.setIsChoose(mViewModel.isChooseAll());
                    if (mViewModel.isChooseAll()) {
                        mViewModel.selectedIds.add(carBean.getCart_info_id());
                    }
                }

                shopCarAdapter.notifyDataSetChanged();
                //选中或者取消全部选中计算
                //重新计算优惠券和总计
                if (mViewModel.isChooseAll())
                    setRecountMoney();
                else {
                    mBinding.tvTotalMoney.setText("合计：¥0.00");
                    mBinding.elDiscountCoupon.setContent("已抵扣0元");
                }
                break;
            case R.id.tv_submit:  //  付款    /   删除

                if (mViewModel.getType() == 0) {
                    if (mViewModel.selectedIds.isEmpty()) {
                        ToastUtils.showShort("请选择要购买的商品");
                        return;
                    }
                    //购物车下单 -->下单成功后返回订单数组 -->传到支付页面
                    showLoading(Sys.LOADING);
                    mViewModel.cartAddOrder(mViewModel.selectedIds);
                } else {
                    //删除选中的商品
                    if (mViewModel.selectedIds.isEmpty()) {
                        ToastUtils.showShort("请选择要删除的商品");
                        return;
                    }
                    showLoading(Sys.LOADING);
                    mViewModel.cartDelete(mViewModel.selectedIds);
                }
                break;
        }
    };

    //购物车列表adapter -->选中/取消选中商品
    private ClickEventHandler<ShopCarBean> shorcarAdapterClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_choose:  // 选中

                bean.setIsChoose(bean.getIsChoose() ? false : true);

                mViewModel.selectedIds.clear();
                boolean chooseAll = true;   //  全选
                boolean nonChoose = true;   //  全不选
                for (int i = 0; i < shopCarAdapter.getData().size(); i++) {
                    ShopCarBean carBean = shopCarAdapter.getData().get(i);
                    if (!carBean.getIsChoose()) {
                        chooseAll = false;
                    }
                    if (carBean.getIsChoose()) {
                        nonChoose = false;
                    }
                    if (carBean.getIsChoose()) {
                        mViewModel.selectedIds.add(carBean.getCart_info_id());
                    }
                }

                mBinding.ivChooseAll.setSelected(chooseAll);
                mViewModel.setChooseAll(chooseAll);

                shopCarAdapter.notifyDataSetChanged();

                //有选中的商品，重新计算优惠券和总计
                if (!nonChoose)
                    setRecountMoney();
                else {
                    mBinding.tvTotalMoney.setText("合计：¥0.00");
                    mBinding.elDiscountCoupon.setContent("已抵扣0元");
                }
                break;
        }
    };

    //店铺中的商品adapter
    private class ShopCarAdapter extends BaseQuickAdapter<ShopCarBean, BaseViewHolder> {
        private ClickEventHandler<ShopCarBean> clickEventHandler;

        public ShopCarAdapter(ClickEventHandler<ShopCarBean> clickEventHandler) {
            super(R.layout.item_shop_car_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, ShopCarBean item) {
            ItemShopCarLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setLiveObservable(mViewModel.aliveObservable);
            binding.setBean(item);
            binding.setUrl(item.getGoods_image());

            binding.tvDoctorHint.setVisibility(TextUtils.isEmpty(item.getDoctor_id()) ? View.GONE : View.VISIBLE);
            binding.tvDoctorName.setVisibility(TextUtils.isEmpty(item.getDoctor_id()) ? View.GONE : View.VISIBLE);

            binding.tvCounselorHint.setVisibility(TextUtils.isEmpty(item.getAdvisers_id()) ? View.GONE : View.VISIBLE);
            binding.tvCounselorName.setVisibility(TextUtils.isEmpty(item.getAdvisers_id()) ? View.GONE : View.VISIBLE);

            binding.ivChoose.setSelected(item.getIsChoose() ? true : false);

            //修改商品数量
            NumberActionView numberActionView = helper.getView(R.id.tv_num);
            //减少 -1
            numberActionView.setBtnDecrease(number -> {
                if (number == 1) {
                    ToastUtils.showShort("数量不能再减少了!");
                } else {
                    number = Math.max(1, --number);
//                    showLoading(PayConstants.LOADING);
                    mViewModel.updateCartNum(item, String.valueOf(number));
                }
            });
            //增加 +1
            numberActionView.setBtnIncrease(number -> {
                String num = String.valueOf(++number);
//                showLoading(PayConstants.LOADING);
                mViewModel.updateCartNum(item, num);
            });
            AppCompatCheckedTextView view = helper.getView(R.id.iv_choose);
            view.setChecked(item.getIsChoose());
        }
    }
}