package com.chengzi.app.ui.mine.activity.vip;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyCouponBindingImpl;
import com.chengzi.app.databinding.ItemMyCouponLayoutBinding;
import com.chengzi.app.ui.mine.bean.CouponListBean;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;

import java.util.List;

/**
 * 我的优惠券
 *
 * @ClassName:MyCouponActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/2 0002   16:14
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class MyCouponActivity extends BaseActivity<ActivityMyCouponBindingImpl, MyVipViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_coupon;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.mRecyclerView.setVisibility(View.VISIBLE);
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyCouponAdapter adapter = new MyCouponAdapter();
        mBinding.mRecyclerView.setAdapter(adapter);

        //我的优惠券
        showLoading(Sys.LOADING);
        mViewModel.couponList();
        mViewModel.couponListLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                List<CouponListBean> data = listResponseBean.getData();
                adapter.setNewData(data);
            } else {
                mBinding.mRecyclerView.setVisibility(View.GONE);
                mBinding.emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    public class MyCouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {
        public MyCouponAdapter() {
            super(R.layout.item_my_coupon_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CouponListBean item) {
            ItemMyCouponLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.executePendingBindings();
        }
    }
}