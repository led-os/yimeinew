package com.chengzi.app.ui.mine.activity.order_beauty_raise;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.adapter.BeautyRaiseAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityBeautyRaiseDetailsBindingImpl;
import com.chengzi.app.databinding.ItemBeautyRaiseDetailsInstitutionsinfoLayoutBinding;
import com.chengzi.app.databinding.ItemBeautyRaiseDetailsUserinfoLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.event.OnBeautyPayStatusChangeEvent;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.activity.verifyorder.VerifyResultActivity;
import com.chengzi.app.ui.mine.bean.UserPlanOrderDetailsBean;
import com.chengzi.app.ui.mine.viewmodel.UserBeautyRaiseViewModel;
import com.chengzi.app.ui.pay.activity.PayActivity;
import com.chengzi.app.utils.DateUtils;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 美人筹订单 详情(普通用户 和 医院 通用) / 美人筹项目详情
 *
 * @ClassName:BeautyRaiseDetailsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.order_beauty_raise
 * @Create On 2019/4/3 0003   11:11
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class BeautyRaiseDetailsActivity extends BaseActivity<ActivityBeautyRaiseDetailsBindingImpl, UserBeautyRaiseViewModel> {

    private String id, plan_id;
    private int type, from;
    private BeautyRaiseDetailsUserInfoAdapter adapter;
    private BeautyRaiseDetailsInstitutionsInfoAdapter adapterHospital;
    private String status;
    //参与机构
    private List<SelectListBean> strings;
    //是否选中医疗机构
    private boolean isChooseHospital = false;
    //参与医院的数量
    private int hospitalSize = 0;

    /**
     * @param context
     * @param id      美人筹订单id
     * @param plan_id (机构取消参与专用)
     * @param type    1用户 4医院
     * @param from    1验证订单
     */
    public static void start(Context context, String id, String plan_id, int type, int from) {
        context.startActivity(new Intent(context, BeautyRaiseDetailsActivity.class)
                .putExtra("id", id)
                .putExtra("plan_id", plan_id)
                .putExtra("type", type)
                .putExtra("from", from)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_beauty_raise_details;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        mBinding.setListener(detailsClickListener);

        id = getIntent().getStringExtra("id");
        from = getIntent().getIntExtra("from", 0);
        plan_id = getIntent().getStringExtra("plan_id");
        type = getIntent().getIntExtra("type", 1);

        initRecyclerView();

        observe();

    }

    @Subscribe
    public void onPayStatusChange(OnBeautyPayStatusChangeEvent event) {
        mViewModel.userPlanOrderDetails(id);
    }

    private void observe() {
        showLoading(Sys.LOADING);
        mViewModel.userPlanOrderDetails(id);
        mViewModel.userPlanOrderDetailsLiveData.observe(this, userPlanOrderDetailsBean -> {
            dismissLoading();
            if (userPlanOrderDetailsBean != null && userPlanOrderDetailsBean.getData() != null) {
                UserPlanOrderDetailsBean data = userPlanOrderDetailsBean.getData();
                //订单信息
                mBinding.setBean(data);
                mBinding.setUrl(data.getOrder_qrcode());
                //订单状态
                status = data.getStatus();
                //发起人id
                String uid = data.getUid();
                mBinding.tvType.setText(data.getStatusName());
                mBinding.tvType2.setText(data.getStatusName());
                //订单支付时间
                mBinding.tvPayTime.setText(!TextUtils.isEmpty(data.getPay_time()) ? DateUtils.dataTime(data.getPay_time(), "yyyy年MM月dd日 HH:mm:ss") : "");
                //参与的用户列表
                List<UserPlanOrderDetailsBean.JoinUserEntity> join_user = data.getJoin_user();
                if (join_user != null && join_user.size() > 0)
                    adapter.setNewData(join_user);
                //参与的机构列表
                List<UserPlanOrderDetailsBean.JoinOrganizationEntity> join_organization = data.getJoin_organization();
                if (join_user != null && join_user.size() > 0) {
                    hospitalSize = join_organization.size();
                    adapterHospital.setNewData(join_organization);
                    strings = new ArrayList<>();
                    for (int i = 0; i < join_organization.size(); i++) {
                        UserPlanOrderDetailsBean.JoinOrganizationEntity joinBean = join_organization.get(i);
                        try {
                            strings.add(new SelectListBean(Integer.parseInt(joinBean.getHospital_id()), joinBean.getUser_name()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    // 是否选中 0-未选中 1-已选中
                    for (int i = 0; i < join_organization.size(); i++) {
                        UserPlanOrderDetailsBean.JoinOrganizationEntity bean = join_organization.get(i);
                        if (bean.getIs_selected().equals("1")) {
                            isChooseHospital = true;
                            break;
                        }
                    }
                } else {
                    hospitalSize = 0;
                }
                //完成度
                String plan = data.getPlan();
                if (plan.contains("%")) {
                    try {
                        mBinding.customProgressBar.setProgress(Integer.parseInt(BeautyRaiseAdapter.deleteString(plan, '%')));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mBinding.customProgressBar.setProgress(0);
                    }
                }
                setStatusOrder(uid);
            }
        });
        //普通用户取消订单
        mViewModel.userPlanOrderCancelLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                //-1全部 0待付款 1未达成 2待使用 3已完成 4已取消
                //刷新  -1 0 1 2 3
                mViewModel.userPlanOrderDetails(id);
                OrderStatusHelp.refreshBeautyOrderList(-1);
                OrderStatusHelp.refreshBeautyOrderList(0);
                OrderStatusHelp.refreshBeautyOrderList(1);
                OrderStatusHelp.refreshBeautyOrderList(2);
                OrderStatusHelp.refreshBeautyOrderList(3);
            }
        });
        //机构取消参与美人筹
        mViewModel.cancelJoinToPlanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                //订单状态 1 进行中 2-已成功 4-未达成
                //刷新  1
//                mViewModel.userPlanOrderDetails(id);
                OrderStatusHelp.refreshBeautyOrderList(1);
                finish();
            }
        });
        //普通用户选择医院
        mViewModel.userPlanChoiceHospitalLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                mViewModel.userPlanOrderDetails(id);
                //刷新  -1 2 3
                OrderStatusHelp.refreshBeautyOrderList(-1);
                OrderStatusHelp.refreshBeautyOrderList(2);
                OrderStatusHelp.refreshBeautyOrderList(3);
                isChooseHospital = true;
            } else {
                dismissLoading();
            }
        });
        //机构确认使用
        mViewModel.orderConfirmUseLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                mViewModel.userPlanOrderDetails(id);
                //订单状态  1-进行中，2-待使用，3-已完成
                //from=1 ->验证订单
                if (from != 1) {
                    OrderStatusHelp.refreshBeautyOrderList(2);
                    OrderStatusHelp.refreshBeautyOrderList(3);
                } else {
                    if (VerifyResultActivity.activity != null)
                        VerifyResultActivity.activity.finish();
                }
            } else {
                dismissLoading();
            }
        });
    }

    // 订单状态 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
    //uid 发起人id
    private void setStatusOrder(String uid) {
        mBinding.tvType.setVisibility(status.equals("3") || status.equals("4") ? View.GONE : View.VISIBLE);
        mBinding.tvType2.setVisibility(status.equals("3") || status.equals("4") ? View.VISIBLE : View.GONE);
        //待付款   取消订单 付款
        if (status.equals("0")) {
            mBinding.tvCancel.setVisibility(View.VISIBLE);
            mBinding.tvInvitedUser.setVisibility(View.VISIBLE);
        } else if (status.equals("1")) {
            //未达成   支付时间 取消订单
            mBinding.llPayTime.setVisibility(View.VISIBLE);
            mBinding.tvCancel.setVisibility(View.VISIBLE);
            if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                mBinding.tvCancel.setText("取消参与");
            }
            //TODO:2019/7/2 从待使用中选择医院到未达成中选择医院->
            if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                //我是发起人 并且 没有选中过选择医院
                mBinding.tvInvited.setVisibility(uid.equals(AccountHelper.getUserId()) && !isChooseHospital && hospitalSize > 0 ? View.VISIBLE : View.GONE);
            }
        } else if (status.equals("2")) {
            //待使用   支付时间 验证信息 取消订单 如果本地id=发起人id则显示选择医院
            mBinding.llPayTime.setVisibility(View.VISIBLE);
            mBinding.clVerifyOrder.setVisibility(View.GONE);
            if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                mBinding.tvCancel.setVisibility(View.VISIBLE);
            } else {
                mBinding.tvInvitedUser.setVisibility(View.GONE);
                mBinding.tvCancel.setVisibility(View.GONE);
                mBinding.tvInvitedUser.setText("确认使用");
            }
//            if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
//                mBinding.tvCancel.setVisibility(View.VISIBLE);
//                //我是发起人 并且 没有选中过选择医院
//                mBinding.tvInvited.setVisibility(uid.equals(AccountHelper.getUserId()) && !isChooseHospital ? View.VISIBLE : View.GONE);
//            } else {
//                mBinding.tvInvitedUser.setVisibility(View.VISIBLE);
//                mBinding.tvCancel.setVisibility(View.GONE);
//                mBinding.tvInvitedUser.setText("确认使用");
//            }
        } else if (status.equals("3") || status.equals("4")) {
            //已完成/已取消  支付时间 验证信息
            mBinding.llPayTime.setVisibility(View.VISIBLE);
            //已取消 + 已选择医院 显示二维码
            if (status.equals("4") && isChooseHospital) {
                mBinding.clVerifyOrder.setVisibility(View.GONE);
            } else if (status.equals("3")) {    //已完成显示二维码
                mBinding.clVerifyOrder.setVisibility(View.GONE);
            } else {    //其他不显示
                mBinding.clVerifyOrder.setVisibility(View.GONE);
            }
            //隐藏确认使用按钮-->待使用(显示)状态刷新时手动隐藏
            mBinding.tvInvitedUser.setVisibility(View.GONE);
        }
    }

    //参与用户/机构信息
    private void initRecyclerView() {
        adapter = new BeautyRaiseDetailsUserInfoAdapter();
        mBinding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mBinding.mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            UserPlanOrderDetailsBean.JoinUserEntity joinUserEntity = adapter.getData().get(position);
            UserHomePageActivity.start(this, joinUserEntity.getUser_id());
        });

        adapterHospital = new BeautyRaiseDetailsInstitutionsInfoAdapter();
        mBinding.mRecyclerViewInstitutions.setAdapter(adapterHospital);
        mBinding.mRecyclerViewInstitutions.setLayoutManager(new GridLayoutManager(this, 4));
        adapterHospital.setOnItemClickListener((adapter2, view, position) -> {
            UserPlanOrderDetailsBean.JoinOrganizationEntity joinOrganizationEntity = adapterHospital.getData().get(position);
            HospitalHomePageActivity.start(this, joinOrganizationEntity.getHospital_id());
        });
    }

    private ClickEventHandler<UserPlanOrderDetailsBean> detailsClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_cancel:   //取消付款/取消参与
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    new MessageDialogBuilder(this)
                            .setMessage("确认取消订单？")
                            .setSureListener(v -> {
                                        showLoading(Sys.LOADING);
                                        mViewModel.userPlanOrderCancel(id);
                                    }
                            )
                            .show();
                } else {
                    new MessageDialogBuilder(this)
                            .setMessage("确认取消参与？")
                            .setSureListener(v -> {
                                        showLoading(Sys.LOADING);
                                        mViewModel.cancelJoinToPlan(plan_id);
                                    }
                            )
                            .show();
                }
                break;
            case R.id.tv_invited:   //选择医院
//                toast("订单发起人->选择医院");
                new SelectListDialog(this, strings, "")
                        .setTitle("选择之后不可修改，请谨慎选择")
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                showLoading(Sys.LOADING);
                                mViewModel.userPlanChoiceHospital(bean.getId(), String.valueOf(causeid));
                            }
                        })
                        .setTvCancle("取消")
                        .show();
                break;
            case R.id.tv_invited_user:   //①付款(普通用户应邀 -->支付) ②确认使用(医院)
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    PayActivity.start(this, id, 2);
                } else { //确认使用
                    new MessageDialogBuilder(this)
                            .setMessage("确认用户已使用？")
                            .setSureListener(v -> {
                                        ////订单类型，1-普通，拼购，秒杀 2-美人筹（必须）
                                        showLoading(Sys.LOADING);
                                        mViewModel.orderConfirmUse(id, "2");
                                    }
                            )
                            .show();
                }
                break;
        }
    };

    //参与用户信息
    public class BeautyRaiseDetailsUserInfoAdapter extends BaseQuickAdapter<UserPlanOrderDetailsBean.JoinUserEntity, BaseViewHolder> {
        public BeautyRaiseDetailsUserInfoAdapter() {
            super(R.layout.item_beauty_raise_details_userinfo_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, UserPlanOrderDetailsBean.JoinUserEntity item) {
            ItemBeautyRaiseDetailsUserinfoLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setUrl(item.getUser_logo());
            binding.executePendingBindings();
            binding.tvOrderName.setText(item.getUser_name());
            binding.tvContent.setText(item.getUser_type() + "(" + item.getPay_status() + ")");
        }
    }

    //参与机构信息
    public class BeautyRaiseDetailsInstitutionsInfoAdapter extends BaseQuickAdapter<UserPlanOrderDetailsBean.JoinOrganizationEntity, BaseViewHolder> {
        public BeautyRaiseDetailsInstitutionsInfoAdapter() {
            super(R.layout.item_beauty_raise_details_institutionsinfo_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, UserPlanOrderDetailsBean.JoinOrganizationEntity item) {
            ItemBeautyRaiseDetailsInstitutionsinfoLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getLogo());
            binding.executePendingBindings();
            // 是否VIP 0-否 1-是
            binding.ivIsVip.setVisibility(item.getIs_VIP().equals("1") ? View.VISIBLE : View.GONE);
            // 选中的机构  is_selected; // 是否选中 0-未选中 1-已选中
            // 2-待使用 3-已完成 4-已取消
            if (status.equals("2") || status.equals("3") || status.equals("4"))
                binding.ivIsSelected.setVisibility(item.getIs_selected().equals("1") ? View.VISIBLE : View.GONE);
        }
    }
}
