package com.chengzi.app.ui.mine.activity.myorder;

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
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyOrderListDetailsBindingImpl;
import com.chengzi.app.databinding.ItemBeautyRaiseDetailsUserinfoLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.OrderPaymentTypeEvent;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.activity.verifyorder.VerifyResultActivity;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.ui.pay.activity.PayOrderActivity;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情-->
 * ① 普通用户（普通订单/拼购订单详情）
 * ② 医院   （验证结果订单详情）            TODO:///type = 3??????
 * ③ 医院订单详情
 *
 * @ClassName:MyOrderListDetailsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/4 0004   11:38
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */

public class MyOrderListDetailsActivity extends BaseActivity<ActivityMyOrderListDetailsBindingImpl, MyOrderListViewModel> {
    //订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）  3-->医院
    private int type, from;
    private String order_id;

    /**
     * @param context
     * @param orderid
     * @param type
     * @param from    1代表验证订单
     */
    public static void start(Context context, String orderid, int type, int from) {
        context.startActivity(new Intent(context, MyOrderListDetailsActivity.class)
                .putExtra("order_id", orderid)
                .putExtra("type", type)
                .putExtra("from", from)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_order_list_details;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(myOrderClickListener);
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", -1);
        from = getIntent().getIntExtra("from", 0);
        observe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(order_id)) {
            showLoading(Sys.LOADING);
            mViewModel.userOrderDetails(order_id);
        }
    }

    private void observe() {
        //订单详情
        mViewModel.userOrderDetailsListLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getData() != null) {
                UserOrderListBean data = responseBean.getData();
                //订单商品的基本信息 -->所有状态都显示的
                mBinding.setBean(data);
                mBinding.setUrl(data.getGoods_image());
                //参与用户信息
                //不显示：普通订单都不显示 //显示：拼购订单全部显示
                //订单类型 1-普通订单 2-拼购订单 3-秒杀订单
                String type = data.getType();
                //订单状态 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消
                String status = data.getStatus();
                // 机构订单状态（结合status使用），默认1-待使用，2-待评价，3-已完成
                String h_status = data.getH_status();
                if (this.type == 3) { //医院订单
                    mBinding.tvOtherStatus.setText(data.getHStatus_name());
                    mBinding.tvCompleteOrCancelStatus.setText(data.getHStatus_name());
                } else {                //普通订单
                    mBinding.tvOtherStatus.setText(data.getStatus_details_name());
                    mBinding.tvCompleteOrCancelStatus.setText(data.getStatus_details_name());
                }
                //是否已投诉，0-未投诉，1-已投诉
                String isComplaint = data.getIsComplaint();
                if (this.type == 3) {
                    setType(data.getUser_info());
                } else {
                    setType(type, data);
                }
                //普通订单显示购买方式 和 医院
//                mBinding.clType.setVisibility(type.equals("1") && this.type == 3 ? View.VISIBLE : View.GONE);
                mBinding.clType.setVisibility(this.type == 1 || this.type == 3 ? View.VISIBLE : View.GONE);
                //显示医生和咨询师是否是VIP
                if (data.getDoctor_info() != null) {
                    mBinding.ivDoctorVip.setVisibility(data.getDoctor_info().getIs_VIP().equals("1") ? View.VISIBLE : View.GONE);
                }
                if (data.getCounselling_info() != null) {
                    mBinding.ivCounselorVip.setVisibility(data.getCounselling_info().getIs_VIP().equals("1") ? View.VISIBLE : View.GONE);
                }
                //是否显示补差价-->3-待使用 4-待评价 5-已完成
                if (this.type != 3) {
                    if (status.equals("3") || status.equals("4") || status.equals("5")) {
                        mBinding.clHasDifference.setVisibility(View.VISIBLE);
                        // 已投诉并且投诉info不等于空  验证时间   我的投诉(投诉对象) -->4-待评价 5-已完成
                        if (status.equals("4") || status.equals("5")) {
                            mBinding.clVerifyTime.setVisibility(View.VISIBLE);
                            if (isComplaint.equals("1") && data.getComplaint_info() != null) {
                                mBinding.clComplaint.setVisibility(View.VISIBLE);
                            }
                        }
                        //
                        //我的评价(医生 咨询师)-->5-已完成
                        if (status.equals("5") && data.getEvaluation() != null) {
                            mBinding.clEvaluation.setVisibility(View.VISIBLE);
                            //医生咨询师（可能都有。可能只有医生。可能只有咨询师）
                            UserOrderListBean.EvaluationEntity evaluation = data.getEvaluation();
                            //① 判断是否有医生评价
                            String doctor_major = evaluation.getDoctor_major();  // 审美
                            String doctor_skill = evaluation.getDoctor_skill();  // 技术
                            mBinding.clEvaluationDoctor.setVisibility(!TextUtils.isEmpty(doctor_major)
                                    && !TextUtils.isEmpty(doctor_skill) ? View.VISIBLE : View.GONE);
//                            mBinding.clEvaluationDoctor.setVisibility(!data.getDoctor_id().equals("0") ? View.VISIBLE : View.GONE);
                            //② 判断是否有咨询师评价
                            String consultant_major = evaluation.getConsultant_major();     // 审美
                            String consultant_service = evaluation.getConsultant_service(); // 服务
                            mBinding.clEvaluationConsultant.setVisibility(!TextUtils.isEmpty(consultant_major)
                                    && !TextUtils.isEmpty(consultant_service) ? View.VISIBLE : View.GONE);
//                            mBinding.clEvaluationConsultant.setVisibility(!data.getCounselling_id().equals("0") ? View.VISIBLE : View.GONE);
                        } else {
                            mBinding.clEvaluation.setVisibility(View.GONE);
                        }
                    }
                    //验证信息-->3-待使用 6已取消(普通订单)
                    if ((status.equals("6") && type.equals("1")) || status.equals("3")) {
                        mBinding.clVerificationInfo.setVisibility(View.VISIBLE);
                        mBinding.setUrlCode(data.getOrder_qrcode());
                    }
                } else {        //医院的订单显示 已补差价（待使用 待评价 已完成）
                    /*
                     // 机构订单状态（结合status使用），默认1-待使用，2-待评价，3-已完成
                        String h_status = data.getH_status();
                    * */
                    mBinding.clHasDifference.setVisibility(View.VISIBLE);
                }
                //显示按钮
                if (this.type != 3) {
                    if (status.equals("5") || status.equals("6")) { //已完成/超时未支付，订单自动取消 不显示按钮
                        mBinding.tvCompleteOrCancelStatus.setVisibility(View.VISIBLE);
                        mBinding.tvOtherStatus.setVisibility(View.GONE);
                        mBinding.llOtherStatus.setVisibility(View.GONE);
                    } else {    //待付款/未达成/待使用/待评价 -->显示按钮
                        mBinding.tvOtherStatus.setVisibility(View.VISIBLE);
                        mBinding.tvCompleteOrCancelStatus.setVisibility(View.GONE);
                        if (status.equals("1")) {
                            //待付款 -->显示 取消订单+付款
                            setShowStatusBtn(true, true, false, false, false);
                        } else if (status.equals("2")) {
                            //未达成 -->显示 取消订单
                            setShowStatusBtn(true, false, false, false, false);
                        } else if (status.equals("3")) {
                            //待使用 -->显示 补差价+取消订单
                            setShowStatusBtn(true, false, true, false, false);
                        } else if (status.equals("4")) {
                            //待评价 -->显示 投诉+评价    0-未投诉，1-已投诉
                            boolean complaints = false;
                            if (isComplaint.equals("0")) {
                                //显示投诉
                                complaints = true;
                            } else {
                                //隐藏投诉
                                complaints = false;
                            }
                            setShowStatusBtn(false, false, false, complaints, true);
                        }
                    }
                } else {    //type=3 医院
                    if (h_status.equals("1")) {     //TODO:隐藏确认使用 防止医院误操作
                        //待使用 -->显示 确认使用
                        setShowStatusBtn(true, false, false);
                    } else if (h_status.equals("2")) {
                        //待评价 -->显示 投诉+评价
                        boolean complaints = false;
                        if (isComplaint.equals("0")) {
                            //显示投诉
                            complaints = true;
                        } else {
                            //隐藏投诉
                            complaints = false;
                        }
                        setShowStatusBtn(false, complaints, true);
                    } else {
                        //已完成 -->不显示
                        mBinding.llOtherStatus.setVisibility(View.GONE);
                        mBinding.tvOtherStatus.setVisibility(View.GONE);
                        //
                        mBinding.tvCompleteOrCancelStatus.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                finish();
                toast("订单错误!");
            }
        });
        //取消订单
        mViewModel.userOrderCancelListLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                //取消成功  //刷新-->当前type(普通/拼购)状态下的 全部/待付款/未达成/待使用
                OrderStatusHelp.refreshOrderList(type, 0);
                OrderStatusHelp.refreshOrderList(type, 1);
                OrderStatusHelp.refreshOrderList(type, 2);
                OrderStatusHelp.refreshOrderList(type, 3);
                //刷新订单详情
                mViewModel.userOrderDetails(order_id);
            }
        });
        //确认使用-->医院
        mViewModel.orderConfirmUseLive.observe(this, responseBean -> {
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                //刷新订单详情
                mViewModel.userOrderDetails(order_id);
                //确认使用成功  //刷新-->医院状态下的 全部/待使用/待评价
                //from=1 ->验证订单
                if (from != 1) {
                    OrderStatusHelp.refreshOrderList(0);
                    OrderStatusHelp.refreshOrderList(1);
                    OrderStatusHelp.refreshOrderList(2);
                } else {
                    if (VerifyResultActivity.activity != null)
                        VerifyResultActivity.activity.finish();
                }
            } else {
                dismissLoading();
            }
        });
    }

    //医院设置用户
    private void setType(UserOrderListBean.UserInfoEntity user_info) {
        mBinding.clBuyTakePartUserInfo.setVisibility(View.VISIBLE);
        mBinding.tvTitle.setText("用户");
        mBinding.tvOrderDetailsPersonNum.setVisibility(View.GONE);
        mBinding.tvTitlePeople.setVisibility(View.GONE);
        List<UserOrderListBean.UserInfoEntity> list = new ArrayList<>();
        list.add(user_info);
        //目标人数->写死两个人
//        mBinding.tvOrderDetailsPersonNum.setText("2人");
        if (list != null && list.size() > 0) {
//            //目标人数
//            mBinding.tvOrderDetailsPersonNum.setText(list.size() + "人");
            //设置数据--
            initRecyclerView(null, list);
        }

    }

    //普通用户设置参与用户信息
    private void setType(String type, UserOrderListBean data) {
        mBinding.clBuyTakePartUserInfo.setVisibility(type.equals("2") ? View.VISIBLE : View.GONE);
        if (type.equals("2")) {
            List<UserOrderListBean.SpellPromoterEntity> list = new ArrayList<>();
            //发起人
            UserOrderListBean.SpellPromoterEntity spell_promoter = data.getSpell_promoter();
            if (spell_promoter != null) {
                list.add(new UserOrderListBean.SpellPromoterEntity(
                        spell_promoter.getUser_id(),
                        spell_promoter.getUser_name(),
                        spell_promoter.getUser_image(),
                        spell_promoter.getPay_status(),
                        "发起人"
                ));
            }
            //参与人
            UserOrderListBean.SpellPromoterEntity spell_participant = data.getSpell_participant();
            if (spell_participant != null) {
                list.add(new UserOrderListBean.SpellPromoterEntity(
                        spell_participant.getUser_id(),
                        spell_participant.getUser_name(),
                        spell_participant.getUser_image(),
                        spell_participant.getPay_status(),
                        "参与人"
                ));
            }
            //目标人数->写死两个人
//        mBinding.tvOrderDetailsPersonNum.setText("2人");
            if (list != null && list.size() > 0) {
//                //目标人数
//                mBinding.tvOrderDetailsPersonNum.setText(list.size() + "人");
                //设置数据--
                initRecyclerView(list, null);
            }
        }
    }

    /**
     * @param cancel     取消
     * @param payment    付款
     * @param difference 补差价
     * @param complaints 投诉
     * @param evaluation 评价
     */
    private void setShowStatusBtn(boolean cancel, boolean payment, boolean difference,
                                  boolean complaints, boolean evaluation) {
        mBinding.llOtherStatus.setVisibility(View.VISIBLE);
        mBinding.tvCancel.setVisibility(cancel ? View.VISIBLE : View.GONE);
        mBinding.tvPayment.setVisibility(payment ? View.VISIBLE : View.GONE);
        mBinding.tvDifference.setVisibility(difference ? View.VISIBLE : View.GONE);
        mBinding.tvComplaints.setVisibility(complaints ? View.VISIBLE : View.GONE);
        mBinding.tvEvaluation.setVisibility(evaluation ? View.VISIBLE : View.GONE);
    }

    /**
     * @param difference 补差价 确认使用
     * @param complaints 投诉
     * @param evaluation 评价
     */
    private void setShowStatusBtn(boolean difference,
                                  boolean complaints, boolean evaluation) {
        mBinding.tvOtherStatus.setVisibility(View.VISIBLE);
        mBinding.llOtherStatus.setVisibility(View.VISIBLE);
        //TODO: 隐藏医院的确认使用 以防止医院误操作
//        mBinding.tvDifference.setText("确认使用");
//        mBinding.tvDifference.setVisibility(difference ? View.VISIBLE : View.GONE);
        mBinding.tvDifference.setVisibility(View.GONE);
        mBinding.tvComplaints.setVisibility(complaints ? View.VISIBLE : View.GONE);
        mBinding.tvEvaluation.setVisibility(evaluation ? View.VISIBLE : View.GONE);
    }

    //参与用户信息-->拼购订单专有
    private void initRecyclerView(List<UserOrderListBean.SpellPromoterEntity> list, List<UserOrderListBean.UserInfoEntity> list1) {
        MyOrderListDetailsUserInfoAdapter adapter = new MyOrderListDetailsUserInfoAdapter();
        mBinding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mBinding.mRecyclerView.setAdapter(adapter);
        if (list != null && list.size() > 0) {
            adapter.setType(1);
            adapter.setNewData(list);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                UserHomePageActivity.start(this, list.get(position).getUser_id());
            });
        } else if (list1 != null && list1.size() > 0) {
            adapter.setType(2);
            adapter.setNewData(list1);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                UserHomePageActivity.start(this, list1.get(position).getUser_id());
            });
        }
    }

    //参与用户信息
    public class MyOrderListDetailsUserInfoAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
        public MyOrderListDetailsUserInfoAdapter() {
            super(R.layout.item_beauty_raise_details_userinfo_layout);
        }

        @Override       //UserInfoEntity SpellPromoterEntity
        protected void convert(BaseViewHolder helper, T item) {
            ItemBeautyRaiseDetailsUserinfoLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            if (type == 1) {
                UserOrderListBean.SpellPromoterEntity item1 = (UserOrderListBean.SpellPromoterEntity) item;
                binding.setUrl(item1.getUser_image());
                binding.tvOrderName.setText(item1.getUser_name());
                binding.tvContent.setText(item1.getPeople_type() + "(" + item1.getPay_status() + ")");
            } else {
                UserOrderListBean.UserInfoEntity item2 = (UserOrderListBean.UserInfoEntity) item;
                binding.setUrl(item2.getLogo());
                binding.tvOrderName.setText(item2.getName());
                binding.tvContent.setText(item2.getMobile());
            }
            binding.executePendingBindings();
        }

        private int type;

        public void setType(int i) {
            this.type = i;
        }
    }

    private ClickEventHandler<UserOrderListBean> myOrderClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_goods_id:  //  商品详情
//                GoodsDetailActivity.start(this, bean.getGoods_id());
                if (bean.getGoods_status().equals("1")) {
                    GoodsDetailActivity.start(this, bean.getGoods_id());
                } else {
                    toast("该商品已下架");
                }
                break;
            case R.id.tv_cancel:  //  取消订单
                new MessageDialogBuilder(this)
                        .setMessage("确认取消订单?")
                        .setSureListener(v -> {
                            showLoading(Sys.LOADING);
                            mViewModel.userOrderCancel(bean.getId());
                        })
                        .show();
                break;
            case R.id.tv_payment:  //  付款
                List<String> order_id = new ArrayList<>();
                order_id.add(bean.getId());
                EventBus.getDefault().postSticky(new OrderPaymentTypeEvent(type));
                PayOrderActivity.start(this, order_id);
                break;
            case R.id.tv_difference:  // 确认使用 补差价
                if (this.type == 3) {
                    new MessageDialogBuilder(this)
                            .setMessage("确认用户已使用？")
                            .setSureListener(v -> {
//                                        toast("确认用户已使用");
                                        showLoading(Sys.LOADING);
                                        mViewModel.orderConfirmUse(bean.getId());
                                    }
                            )
                            .show();
                } else {
                    PriceSpreadActivity.start(this, bean.getId(), type);
                }
                break;
            case R.id.tv_complaints:  //  投诉
                ComplaintsActivity.start(this, bean.getId(), type);
                break;
            case R.id.tv_evaluation:  //  评价
                if (AccountHelper.getIdentity() == Sys.TYPE_USER)   //普通用户->我的订单
                    startActivity(new Intent(this, EvaluationActivity.class)
                            .putExtra("type", type)         //刷新订单时 使用
                            .putExtra("order_id", bean.getId())
                    );
                else
                    startActivity(new Intent(this, HospitalEvaluationActivity.class)
                            .putExtra("order_id", bean.getId())
                    );
                break;
        }
    };
}
