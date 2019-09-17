package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentGoodsSearchPromoteBindingImpl;
import com.chengzi.app.databinding.ItemGoodsSearchPromoteLayoutBinding;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.ui.mine.activity.popularize.IWantPopularizeActivity;
import com.chengzi.app.ui.mine.bean.ProductPromotionProductBean;
import com.chengzi.app.ui.mine.viewmodel.GoodsSearchPromoteViewModel;

/**
 * 个人中心 医院 我要推广 商品站内搜索推广->商品
 *
 * @ClassName:GoodsSearchPromtionActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   14:12
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */

public class GoodsSearchPromoteFragment extends LazyloadFragment<FragmentGoodsSearchPromoteBindingImpl, GoodsSearchPromoteViewModel> {
    //分类id
    private String cate_id;
    private PagingLoadHelper helper;
    //设置的限额
    private String money;
    //设置的限额
//    private TextView tvPromoteMoney;
    private int position;
    //是否推广
    private ImageView ivPromote;
    private boolean is_extension;
    private ProductPromotionProductBean productPromotionProductBean;
    private GoodsSearchPromoteAdapter adapter;

    //分类id
    public static GoodsSearchPromoteFragment newInstance(String cate_id) {
        GoodsSearchPromoteFragment fragment = new GoodsSearchPromoteFragment();
        Bundle args = new Bundle();
        args.putString("cate_id", cate_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_goods_search_promote;
    }

    @Override
    public void onLazyload() {
        if (!TextUtils.isEmpty(cate_id)) {
            helper.start();
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            cate_id = arguments.getString("cate_id");
        }
        adapter = new GoodsSearchPromoteAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        binding.swipeRefreshView.setAdapter(adapter);
        ((SimpleItemAnimator) binding.swipeRefreshView.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);

        viewModel.cate_id = cate_id;

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        //设置限额  推广
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            productPromotionProductBean = adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_promote:   // 推广
                    ivPromote = (ImageView) adapter1.getViewByPosition(binding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote);
                    is_extension = productPromotionProductBean.isIs_promotion();
                    showLoading(Sys.LOADING);
                    if (!is_extension) {  //开启  1-商品
                        viewModel.addSearchPromotionItem(productPromotionProductBean.getId(), "1");
                    } else {
                        viewModel.removeSearchPromotionItem(productPromotionProductBean.getId(), "1");
                    }
                    break;
                case R.id.tv_set:       // 设置
                    this.position = position;
                    new SetLimitDialog(getContext(), 2)
                            .setSureListener(text -> {
//                            tv_promote_money.setText("¥" + text);
//                                tvPromoteMoney = (TextView) adapter1.getViewByPosition(binding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote_money);
                                money = text;
                                ////推广限额:如果不为空时 判断是不是小于推广金额
                                if (!TextUtils.isEmpty(money)) {
                                    double v = Double.parseDouble(money);
                                    if (v < IWantPopularizeActivity.promotion_price) {
                                        toast(getResources().getString(R.string.want_popularize));
                                        return;
                                    }
                                }
                                showLoading(Sys.LOADING);  //5案例
                                viewModel.setSearchPromotionAmount(productPromotionProductBean.getId(), "1", text);
                            })
                            .show();
                    break;
            }
        });
        observe();
    }

    private void observe() {
        viewModel.productPromotionProductLiveData.observe(this, productPromotionProductBeans -> {
            if (productPromotionProductBeans != null && productPromotionProductBeans.size() > 0) {
                helper.onComplete(productPromotionProductBeans);
            } else {
                helper.onComplete(null);
            }
        });
        //设置限额
        viewModel.setSearchPromotionAmountLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                tvPromoteMoney.setText("¥" + money);
                adapter.getData().get(position).setPreset_amount(money);
                adapter.notifyItemChanged(position);
            }
        });
        //开启推广
        viewModel.addSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(true);
                productPromotionProductBean.setIs_promotion(true);
            }
        });
        //关闭推广
        viewModel.removeSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(false);
                productPromotionProductBean.setIs_promotion(false);
            }
        });
    }

    public class GoodsSearchPromoteAdapter extends BaseQuickAdapter<ProductPromotionProductBean, BaseViewHolder> {
        public GoodsSearchPromoteAdapter() {
            super(R.layout.item_goods_search_promote_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ProductPromotionProductBean item) {
            ItemGoodsSearchPromoteLayoutBinding bindings = DataBindingUtil.bind(helper.itemView);
//            bindings.setListener(clickEventHandler);
            bindings.setBean(item);
            bindings.setUrl(item.getImage());
            bindings.executePendingBindings();

            //是否推广
            boolean is_extension = item.isIs_promotion();
            bindings.tvPromote.setSelected(is_extension);

            helper.addOnClickListener(R.id.tv_set)
                    .addOnClickListener(R.id.tv_promote);
        }
    }
}