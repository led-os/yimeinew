package com.chengzi.app.ui.mine.activity.popularize;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDoctorOrCounselorPromotionBindingImpl;
import com.chengzi.app.databinding.ItemDoctorOrCounselorPromotionLayoutBinding;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.bean.DoctorPromotionBean;
import com.chengzi.app.ui.mine.viewmodel.IWantPopularizeViewModel;

import java.util.ArrayList;

/**
 * 个人中心 医院 我要推广 医生/咨询师站内搜索推广
 *
 * @ClassName:DoctorOrCounselorPromotionActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   17:28
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */

public class DoctorOrCounselorPromotionActivity extends BaseActivity<ActivityDoctorOrCounselorPromotionBindingImpl, IWantPopularizeViewModel> {
    private PagingLoadHelper helper;
    private int type;
    private DoctorOrCounselorPromotionAdapter adapter;

    //2医生 3咨询师
    public static void start(Context context, int type) {
        context.startActivity(new Intent(context, DoctorOrCounselorPromotionActivity.class)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_or_counselor_promotion;
    }

    //设置的限额
    private String money;
    //设置的限额
//    private TextView tvPromoteMoney;
    private int postion = 0;
    //是否推广
    private ImageView ivPromote;
    private boolean is_extension;
    private DoctorPromotionBean promotionBean;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);

        mBinding.topBar.setCenterText(type == 2 ? "医生推广" : "咨询师推广");
        adapter = new DoctorOrCounselorPromotionAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        mBinding.swipeRefreshView.setAdapter(adapter);
        ((SimpleItemAnimator) mBinding.swipeRefreshView.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        mViewModel.doctorOrCounselorPromotion = type;
        helper.start();

        //设置限额  推广
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            promotionBean = adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_promote:   // 推广
                    ivPromote = (ImageView) adapter1.getViewByPosition(mBinding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote);
                    is_extension = promotionBean.isIs_extension();
                    showLoading(Sys.LOADING);
                    if (!is_extension) {  //开启  type 2医生 3咨询师   --->2医生 3咨询师
                        mViewModel.addSearchPromotionItem(promotionBean.getId(), String.valueOf(2));
                    } else {
                        mViewModel.removeSearchPromotionItem(promotionBean.getId(), String.valueOf(3));
                    }
                    break;
                case R.id.tv_set:       // 设置
                    this.postion = position;
                    new SetLimitDialog(this, 2)
                            .setSureListener(text -> {
//                            tv_promote_money.setText("¥" + text);
//                                tvPromoteMoney = (TextView) adapter1.getViewByPosition(mBinding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote_money);
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
                                mViewModel.setSearchPromotionAmount(promotionBean.getId(), String.valueOf(type ), text);
                            })
                            .show();
                    break;
            }
        });
        observe();
    }

    private void observe() {
        //医生 咨询师列表
        mViewModel.doctorOrConsultantPromotionLive.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else
                helper.onComplete(new ArrayList<>());
        });
        //设置限额
        mViewModel.setSearchPromotionAmountLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                tvPromoteMoney.setText("¥" + money);
                adapter.getData().get(postion).setPreset_amount(money);
                adapter.notifyItemChanged(postion);
            }
        });
        //开启推广
        mViewModel.addSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(true);
                promotionBean.setIs_extension(true);
            }
        });
        //关闭推广
        mViewModel.removeSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(false);
                promotionBean.setIs_extension(false);
            }
        });
    }

    public class DoctorOrCounselorPromotionAdapter extends BaseQuickAdapter<DoctorPromotionBean, BaseViewHolder> {
        public DoctorOrCounselorPromotionAdapter() {
            super(R.layout.item_doctor_or_counselor_promotion_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorPromotionBean item) {
            ItemDoctorOrCounselorPromotionLayoutBinding bindings = DataBindingUtil.bind(helper.itemView);
            bindings.setListener(clickEventHandler);
            bindings.setBean(item);
            bindings.setUrl(item.getImg());
            bindings.setType(mViewModel.doctorOrCounselorPromotion);
            bindings.executePendingBindings();
            //是否推广
            boolean is_extension = item.isIs_extension();
            bindings.tvPromote.setSelected(is_extension);

            helper.addOnClickListener(R.id.tv_set)
                    .addOnClickListener(R.id.tv_promote);
        }
    }

    private ClickEventHandler<DoctorPromotionBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_doctor_or_counselor:  //医生/咨询师主页
                if (type == 2) {   // 医生主页
                    DoctorHomePageActivity.startDoctor(this, bean.getId());
                } else {           // 咨询师主页
                    DoctorHomePageActivity.startCounselor(this, bean.getId());
                }
                break;
        }
    };
}
