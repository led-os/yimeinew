package com.chengzi.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemMainGoodsLayoutBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.GoodBean;

public class MainGoodsAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder>
        implements ClickEventHandler<GoodBean> {

    private int orientation = LinearLayoutManager.VERTICAL;

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public MainGoodsAdapter() {
        super(R.layout.item_main_goods_layout);
    }

    //找商品中穿过来2 商品搜索推广
    public MainGoodsAdapter(int type) {
        super(R.layout.item_main_goods_layout);
        this.type = type;
    }

    private int type = 0;

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            View itemView = baseViewHolder.itemView;
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params != null) {
                params.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(50)) / 3 + 1;
                itemView.setLayoutParams(params);
            }
        }
        return baseViewHolder;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {

        Context context = helper.itemView.getContext();

        ItemMainGoodsLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setListener(this);

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(11, true);
        spanBuilder.append(context.getString(R.string.rmb_symbol), absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spanBuilder.append(context.getString(R.string.price_place_holder, item.getBuy_price()));

        SpannableString ss = new SpannableString(context.getString(R.string.buy_num, item.getOrderNum()));
        ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.color_9));
        ss.setSpan(fcs, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        absoluteSizeSpan = new AbsoluteSizeSpan(11, true);
        ss.setSpan(absoluteSizeSpan, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spanBuilder.append(ss);

        binding.tvPriceAndBuyNum.setText(spanBuilder);

    }

    @Override
    public void handleClick(View view, GoodBean bean) {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        //需要推广id (1-首页其他 2-vip精选 3-vip其他 4-找商品)
        GoodsDetailActivity.start(view.getContext(), bean.getId(), bean.getPromotion_id(), type);
    }
}