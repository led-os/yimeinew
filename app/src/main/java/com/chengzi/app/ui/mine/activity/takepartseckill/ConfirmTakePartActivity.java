package com.chengzi.app.ui.mine.activity.takepartseckill;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.utils.CommonUtils;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityConfirmTakePartBindingImpl;
import com.chengzi.app.databinding.ItemConfirmTakePartLayoutBinding;
import com.chengzi.app.databinding.ItemTakePartSeckillLayoutBinding;
import com.chengzi.app.ui.mine.bean.KillHospitalListBean;
import com.chengzi.app.ui.mine.viewmodel.TakePartSeckillViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认参与
 *
 * @ClassName:ConfirmTakePartActivity
 * @PackageName:com.yimei.app.ui.mine.activity.takepartseckill
 * @Create On 2019/4/12 0012   10:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */
public class ConfirmTakePartActivity extends BaseActivity<ActivityConfirmTakePartBindingImpl, TakePartSeckillViewModel> {

    private KillHospitalListBean bean;
    //分类id
    private String cate_id;

    public static void start(Context context, KillHospitalListBean bean, String cate_id) {
        context.startActivity(new Intent(context, ConfirmTakePartActivity.class)
                .putExtra("bean", bean)
                .putExtra("cate_id", cate_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_confirm_take_part;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setViewModel(mViewModel);

        cate_id = getIntent().getStringExtra("cate_id");

        bean = (KillHospitalListBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            //赋值添加的数据
            mViewModel.goods_id.set(bean.getGoods_id());

            mBinding.include.setBean(bean);
            mBinding.include.setUrl(bean.getLogo());
            mBinding.include.tvMoney.setText(bean.getPractical_price());

            //设置 秒杀的商品
            setConfirmTakePartItem();
            //设置 时间段
            setTimeRecyclerView();
            //秒杀价格 (保留小数点后两位)et_seckill_money
            CommonUtils.setPoint(mBinding.etSeckillMoney, 2);
            mBinding.etSeckillMoney.setSelection(mBinding.etSeckillMoney.length());
            mBinding.etSeckillNum.setSelection(mBinding.etSeckillNum.length());
        } else {
            toast("该商品有误!");
            finish();
        }
    }

    private void setConfirmTakePartItem() {
        ItemTakePartSeckillLayoutBinding layout = mBinding.include;
        layout.tvOriginalPrice.setVisibility(View.GONE);
        layout.llAttention.setVisibility(View.GONE);
        layout.tvCancleTakePart.setVisibility(View.GONE);
    }

    private void setTimeRecyclerView() {
        mBinding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ConfirmTakePartAdapter adapter = new ConfirmTakePartAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        mBinding.mRecyclerView.setAdapter(adapter);
        List<String> strings = new ArrayList<>();
        strings.add("10:00");
        strings.add("12:00");
        strings.add("14:00");
        strings.add("16:00");
        strings.add("18:00");
        strings.add("20:00");
        strings.add("22:00");
        strings.add("00:00");
        adapter.setNewData(strings);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
//            toast(strings.get(position) + "");
            mViewModel.time.set(strings.get(position));
            adapter.notifyDataSetChanged();
        });

        mViewModel.killAddLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                toast("参与成功!");
                OrderStatusHelp.refreshTakePartSeckillList(cate_id);
                finish();
            }
        });
    }

    public class ConfirmTakePartAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public ConfirmTakePartAdapter() {
            super(R.layout.item_confirm_take_part_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ItemConfirmTakePartLayoutBinding headerLayoutBinding = DataBindingUtil.bind(helper.itemView);
            headerLayoutBinding.setText(item);
            if (item.equals(mViewModel.time.get())) {
                headerLayoutBinding.tvTime.setBackgroundResource(R.drawable.rect_btn_red_background);
                headerLayoutBinding.tvTime.setTextColor(Color.WHITE);
            } else {
                headerLayoutBinding.tvTime.setBackgroundResource(R.drawable.app_navigator_item_bg);
                headerLayoutBinding.tvTime.setTextColor(Color.parseColor("#FF333333"));
            }
        }
    }

    private ClickEventHandler<Object> clickListener = (view, beans) -> {
        switch (view.getId()) {
            case R.id.tv_confirm_take_part:  //确认参与   //

                if (TextUtils.isEmpty(mViewModel.goods_id.get())) {
                    toast("商品不能为空!");
                    break;
                }
                if (TextUtils.isEmpty(mViewModel.time.get())) {
                    toast("请选择参与明天秒杀时段!");
                    break;
                }
                if (TextUtils.isEmpty(mViewModel.price.get())) {
                    toast("请输入秒杀价!");
                    break;
                }
                if (TextUtils.isEmpty(mViewModel.num.get())) {
                    toast("请输入限购数量!");
                    break;
                }
                showLoading(Sys.LOADING);
                mViewModel.killAdd(cate_id);
                break;
        }
    };
}