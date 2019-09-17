package com.chengzi.app.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.hwangjr.rxbus.RxBus;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentAdvPromoteBindingImpl;
import com.chengzi.app.ui.mine.activity.popularize.AdvPromoteSnapUpActivity;
import com.chengzi.app.ui.mine.bean.BannerPromotionListBean;
import com.chengzi.app.ui.mine.viewmodel.AdvPromoteViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

/**
 * 个人中心 医院 我要推广  banner广告推广
 *
 * @ClassName:AdvPromoteFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/13 0013   10:43
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */

public class AdvPromoteFragment extends LazyloadFragment<FragmentAdvPromoteBindingImpl, AdvPromoteViewModel>
        implements OrderStatusHelp.PromoteTypeChange {

    //分类id
    private String cate_id;
    private String cate_name;
    //普通用户common  vip用户vip
    private String type;
    // 专区类型 （1.普通;2.VIP）
    private String zone_type;
//    private BannerPromotionListBean.BannerListEntity bannerListEntity;

    public static AdvPromoteFragment newInstance(String cate_id, String type, String zone_type, String cate_name) {
        AdvPromoteFragment fragment = new AdvPromoteFragment();
        Bundle args = new Bundle();
        args.putString("cate_id", cate_id);
        args.putString("type", type);
        args.putString("zone_type", zone_type);
        args.putString("cate_name", cate_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void typeChange(String zone_type, String cate_id) {
        if (this.zone_type.equals(String.valueOf(zone_type))
                && this.cate_id.equals(String.valueOf(cate_id))) {
            showLoading(Sys.LOADING);
            viewModel.bannerPromotionList(zone_type, cate_id);
        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_adv_promote;
    }

    @Override
    public void onLazyload() {
        showLoading(Sys.LOADING);
        viewModel.bannerPromotionList(zone_type, cate_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperObservableManager.unregisterObserver(OrderStatusHelp.PromoteTypeChange.class, this);
        RxBus.get().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(clickListener);
        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.PromoteTypeChange.class, this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            cate_id = arguments.getString("cate_id");
            cate_name = arguments.getString("cate_name");
            type = arguments.getString("type");
            zone_type = arguments.getString("zone_type");
        }
        viewModel.bannerPromotionListLiveData.observe(this, bannerPromotionListBean -> {
            dismissLoading();
            if (bannerPromotionListBean != null && bannerPromotionListBean.getData() != null) {
                BannerPromotionListBean data = bannerPromotionListBean.getData();
                //是否可抢
                if (data.isCanUse()) {  //可抢
                    binding.tvSnapUp.setVisibility(View.VISIBLE);
                    binding.tvHasOccupied.setVisibility(View.GONE);
                } else { //不可抢 -->显示机构
                    binding.tvHasOccupied.setVisibility(View.VISIBLE);
                    binding.tvSnapUp.setVisibility(View.GONE);
                    binding.setBean(data);
                    //不可抢
                    /*
                    *  /*
    status: 表示当前用户抢广告位的状态，取值如下
        canUse = 可以抢
        gradeLess = 因为评分过低，不能抢
        isNotVip = 因为不是VIP，不能抢
        isFull = 因为五个数值已经满了，不能抢
        isHas = 因为已经抢了一个了，不能抢
    * */
                    String statuss = data.getStatuss();
                    if (statuss.equals("isHas") || statuss.equals("isFull"))
                        binding.setUrl(data.getBannerImg());
                }
            }
        });
    }

    private ClickEventHandler<BannerPromotionListBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_snap_up:  //抢--> 广告推广 c_type=1
                AdvPromoteSnapUpActivity.start(getContext(), cate_id, zone_type, "1", cate_name);
                break;
        }
    };
}