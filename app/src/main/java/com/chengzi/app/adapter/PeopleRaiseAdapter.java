package com.chengzi.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemPeopleRaiseLayoutBinding;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;

public class PeopleRaiseAdapter extends BaseQuickAdapter<RaiseBean, BaseViewHolder> {

    //    private static final String[] STATUS = {"待付款", "未达成", "待使用 ", "已完成", "已取消"};
    private static final String[] STATUS = {"待付款", "进行中", "已成功 ", "已完成", "未达成"};

    public PeopleRaiseAdapter() {
        super(R.layout.item_people_raise_layout);
    }

    private boolean isDetailRecommend;

    public void setDetailRecommend(boolean detailRecommend) {
        isDetailRecommend = detailRecommend;
    }

    @Override
    protected void convert(BaseViewHolder helper, RaiseBean item) {

        Context context = helper.itemView.getContext();

        ItemPeopleRaiseLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

        if (isDetailRecommend) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) binding.getRoot().getLayoutParams();
            params.leftMargin = params.rightMargin = SizeUtils.dp2px(15);
            params.topMargin = params.bottomMargin = SizeUtils.dp2px(5);
            binding.getRoot().setPadding(SizeUtils.dp2px(5)
                    , SizeUtils.dp2px(10)
                    , SizeUtils.dp2px(5)
                    , SizeUtils.dp2px(10));
            binding.getRoot().setBackgroundResource(R.drawable.bottom_line_bg);
        }

        binding.setBean(item);
        binding.setStatus(item.getStatus());

        String price = item.getPrice(); // 单人价格
        price = price == null ? "" : price;

        SpannableStringBuilder builder = new SpannableStringBuilder();

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
        builder.append(context.getString(R.string.rmb_symbol), absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
        builder.append(price);
        builder.append(context.getString(R.string.per_person), absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.tvPrice.setText(builder);

        int progress = 0;
        String plan = item.getPlan();
        if (!TextUtils.isEmpty(plan)) {
            String replace = plan.replace("%", "");
            progress = Integer.valueOf(replace);
        }
        binding.customProgressBar.setProgress(progress);

        int status = item.getStatus();
        if (status >= 0 && status < 5) {
            binding.tvStatus.setText(STATUS[status]);
        }

        binding.executePendingBindings();

    }

}
