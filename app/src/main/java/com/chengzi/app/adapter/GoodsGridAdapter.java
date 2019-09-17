package com.chengzi.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemSearchGoodsGridLayoutBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.GoodBean;

public class GoodsGridAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> implements ClickEventHandler<GoodBean> {

    //是否显示医院名称
    //显示：搜索商品--结果页
    //不显示：机构主页中的特色商品和其他商品
    private boolean showHospitalName = true;

    public GoodsGridAdapter() {
        super(R.layout.item_search_goods_grid_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {

        Context context = helper.itemView.getContext();

        ItemSearchGoodsGridLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setListener(this);
        binding.setBean(item);
        binding.setShow(showHospitalName);

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(11, true);
        spanBuilder.append(context.getString(R.string.rmb_symbol), absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spanBuilder.append(context.getString(R.string.price_place_holder, item.getBuy_price()));
        spanBuilder.append(context.getString(R.string.hand_blank));
        spanBuilder.append(context.getString(R.string.hand_blank));

        SpannableString ss = new SpannableString(context.getString(R.string.buy_num, item.getOrderNum()));
        ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.color_9));
        ss.setSpan(fcs, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        absoluteSizeSpan = new AbsoluteSizeSpan(11, true);
        ss.setSpan(absoluteSizeSpan, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spanBuilder.append(ss);

        binding.tvPriceAndBuyNum.setText(spanBuilder);

        binding.executePendingBindings();

    }

    @Override
    public void handleClick(View view, GoodBean bean) {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        GoodsDetailActivity.start(view.getContext(), bean.getId(), bean.getPromotion_id(), 2);
    }

    public void setShowHospitalName(boolean showHospitalName) {
        this.showHospitalName = showHospitalName;
    }
}
