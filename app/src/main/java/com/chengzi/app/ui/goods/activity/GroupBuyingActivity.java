package com.chengzi.app.ui.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.GroupBuyAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityGroupBuyingBinding;
import com.chengzi.app.databinding.ItemGroupBuyingHeaderLayoutBinding;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.viewmodel.GroupBuyingViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 正在拼购
 *
 * @ClassName:GroupBuyingActivity
 * @PackageName:com.yimei.app.ui.goods.activity
 * @Create On 2019/4/16 0016   17:16
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class GroupBuyingActivity extends BaseActivity<ActivityGroupBuyingBinding,
        GroupBuyingViewModel> {

    private static final String EXTRA_IS_AD = "extra_is_ad";

    public static void start(Context context, String goodsId, boolean isAd) {
        Intent intent = new Intent(context, GroupBuyingActivity.class);
        intent.putExtra(Sys.EXTRA, goodsId)
                .putExtra(EXTRA_IS_AD, isAd);
        context.startActivity(intent);
    }

    private PagingLoadHelper helper;
    private boolean isFirstIn = true;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_group_buying;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        String goodsId = getIntent().getStringExtra(Sys.EXTRA);
        mViewModel.setGoodsId(goodsId);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecycler();

        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });

    }

    @Subscribe(sticky = true)
    public void fetchData(GoodDetailBean bean) {
        mViewModel.setGoodDetailBean(bean);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstIn) {
            helper.start();
        } else {
            helper.onPulldown();
        }
        isFirstIn = false;
    }

    private void initRecycler() {
        GroupBuyAdapter adapter = new GroupBuyAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayout.VERTICAL));

        ItemGroupBuyingHeaderLayoutBinding headerBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.item_group_buying_header_layout,
                mBinding.swipeRefreshView.getRecyclerView(), false);

        bindGoodsInfo(headerBinding);

        adapter.addHeaderView(headerBinding.getRoot());
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.btn_join_group_buy:   //  去拼购

                    if (AccountHelper.shouldLogin(this)) {
                        return;
                    }

                    GoodDetailBean bean = mViewModel.getGoodDetailBean();
                    if (bean == null) {
                        return;
                    }

                    boolean isAd = getIntent().getBooleanExtra(EXTRA_IS_AD, false);

                    EventBus.getDefault().postSticky(bean);
                    SpellBean item = adapter.getItem(position);
                    ConfirmGoodsOrderActivity.start(this, item, ConfirmGoodsOrderActivity.ORDER_TYPE_SPELL_JOIN, isAd);
                    break;
            }
        });
    }

    private void bindGoodsInfo(ItemGroupBuyingHeaderLayoutBinding headerBinding) {
        headerBinding.setBean(mViewModel.getGoodDetailBean());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
        spannableStringBuilder.append("拼团价  ¥", absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(mViewModel.getGoodDetailBean().getSpell_price());

        headerBinding.tvPrice.setText(spannableStringBuilder);

        headerBinding.executePendingBindings();
    }

}
