package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.utils.CommonUtils;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentChooseGoodsBindingImpl;
import com.chengzi.app.databinding.IncludeZonePromoteLayoutBinding;
import com.chengzi.app.ui.mine.bean.PromotionSelectProductBean;
import com.chengzi.app.ui.mine.viewmodel.ChooseGoodsViewModel;

/**
 * 抢推广 选择商品
 *
 * @ClassName:ChooseGoodsFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/15 0015   11:35
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class ChooseGoodsFragment extends LazyloadFragment<FragmentChooseGoodsBindingImpl, ChooseGoodsViewModel> {

    //专区id
    private String cate_id;
    //选中的专区id
    private String choose_cate_id;
    //1普通专区 2VIP专区
    private int position;
    private PagingLoadHelper helper;

    public static ChooseGoodsFragment newInstance(String cate_id, int chooseposition, String choose_cate_id) {
        ChooseGoodsFragment fragment = new ChooseGoodsFragment();
        Bundle args = new Bundle();
        args.putString("cate_id", cate_id);
        args.putString("choose_cate_id", choose_cate_id);
        args.putInt("chooseposition", chooseposition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_choose_goods;
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
            choose_cate_id = arguments.getString("choose_cate_id");
            position = arguments.getInt("chooseposition", 0);
        }
        ChooseGoodsAdapter adapter = new ChooseGoodsAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        //选中的可选择的id
        viewModel.cate_id = choose_cate_id;
        //查看的id
        viewModel.pid = cate_id;

        binding.swipeRefreshView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(CommonUtils.dip2px(getContext(), 10)));
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.productPromotionProductLiveData.observe(this, productPromotionProductBeans -> {
            if (productPromotionProductBeans != null && productPromotionProductBeans.size() > 0) {
                helper.onComplete(productPromotionProductBeans);
            } else {
                helper.onComplete(null);
            }
        });
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.top = space;
        }
    }

    public class ChooseGoodsAdapter extends BaseQuickAdapter<PromotionSelectProductBean, BaseViewHolder> {
        public ChooseGoodsAdapter() {
            super(R.layout.include_zone_promote_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, PromotionSelectProductBean item) {
            IncludeZonePromoteLayoutBinding bindings = DataBindingUtil.bind(helper.itemView);
            bindings.setListener(clickEventHandler);
            bindings.setUrl(item.getImage());
            bindings.setBean(item);
            bindings.executePendingBindings();
//            if (choose_cate_id.equals(item.getCategory_id())) {
//                bindings.tvRevocation.setVisibility(View.VISIBLE);
//                bindings.tvRevocation.setText("选择");
//            } else {
//                bindings.tvRevocation.setVisibility(View.GONE);
//            }
            //// 该商品是否可以用来推广
            boolean canUseToPromotion = item.isCanUseToPromotion();
            //// 当前商品是否可选择用来在 cate_id 指定的分类里进行推广
            boolean canChoose = item.isCanChoose();
            if (canUseToPromotion && canChoose) {
                bindings.tvRevocation.setVisibility(View.VISIBLE);
                bindings.tvRevocation.setText("选择");
            } else {
                bindings.tvRevocation.setVisibility(View.GONE);
            }
        }
    }

    private ClickEventHandler<PromotionSelectProductBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_revocation:  //  选择
                //回显
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("model_id", bean.getId());
                bundle.putString("cate_id", cate_id);
                bundle.putString("goods_image", bean.getImage());
//                bundle.putString("buy_price", bean.getBuy_price());
                bundle.putSerializable("data", bean);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                getActivity().setResult(200, intent);
                getActivity().finish();
                break;
        }
    };
}