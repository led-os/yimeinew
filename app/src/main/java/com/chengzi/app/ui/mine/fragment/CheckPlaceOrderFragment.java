package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentCheckPlaceOrderBindingImpl;
import com.chengzi.app.databinding.ItemPlaceOrderLayoutBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.SeeOrderBean;
import com.chengzi.app.ui.mine.viewmodel.CheckPlaceOrderModel;

import java.util.ArrayList;

/**
 * 下单我的
 *
 * @ClassName:CheckComplaintsFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/30 0030   16:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/30 0030 handongkeji All rights reserved.
 */

public class CheckPlaceOrderFragment extends LazyloadFragment<FragmentCheckPlaceOrderBindingImpl, CheckPlaceOrderModel> {
    private PagingLoadHelper helper;

    public static CheckPlaceOrderFragment newInstance() {
        CheckPlaceOrderFragment fragment = new CheckPlaceOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_check_place_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        CheckPlaceOrderAdapter adapter = new CheckPlaceOrderAdapter();
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.seeOrderLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class CheckPlaceOrderAdapter extends BaseQuickAdapter<SeeOrderBean, BaseViewHolder> {

        public CheckPlaceOrderAdapter() {
            super(R.layout.item_place_order_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, SeeOrderBean item) {
            ItemPlaceOrderLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.setUrlOrderImage(item.getGoods_image());
            binding.executePendingBindings();
            //tv_sex性别
            String uu_gender = item.getGender();
            binding.tvSex.setBackgroundResource(uu_gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(uu_gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);
        }
    }

    private ClickEventHandler<SeeOrderBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getUser_id());
                break;
            case R.id.ll_place_an_order:  //  下单我的-->商品详情
                //    ////商品状态 1-上架 2-下架
                if (bean.getGoods_status().equals("1")) {
                    GoodsDetailActivity.start(getContext(), bean.getGoods_id());
                } else {
                    toast("该商品已下架");
                }
                break;
        }
    };
}
