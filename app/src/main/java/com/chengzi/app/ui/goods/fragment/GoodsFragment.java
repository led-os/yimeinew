package com.chengzi.app.ui.goods.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.netease.nim.uikit.api.yimei.ConsultantBean;
import com.netease.nim.uikit.api.yimei.ConsultantViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentGoodsBinding;
import com.chengzi.app.dialog.AddToCarSelectDoctorDialog;
import com.chengzi.app.ui.goods.activity.ConfirmGoodsOrderActivity;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;
import com.chengzi.app.ui.homepage.fragment.SubEstimateFragment;
import com.chengzi.app.ui.homepage.fragment.SubPeopleSayFragment;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.utils.NimUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 商品
 *
 * @ClassName:GoodsFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/4/16 0016   10:16
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class GoodsFragment extends BaseFragment<FragmentGoodsBinding,
        GoodsDetailViewModel> implements View.OnClickListener, RefreshListenerRegistry {

    private List<OnRefreshListener> listeners = new ArrayList<>();

    public GoodsFragment() {
    }

    public static GoodsFragment newInstance() {
        android.os.Bundle args = new Bundle();
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_goods;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        if (AccountHelper.getIdentity() > 1) {
            binding.btnOnlineServices.setVisibility(View.INVISIBLE);
            binding.btnOnlineServices.setEnabled(false);
            binding.btnAddShopcar.setVisibility(View.INVISIBLE);
            binding.btnAddShopcar.setEnabled(false);
            binding.btnRushPurchase.setVisibility(View.INVISIBLE);
            binding.btnRushPurchase.setEnabled(false);
            binding.btnGroupBuying.setVisibility(View.GONE);
            binding.btnGroupBuying.setEnabled(false);
        }

        if (savedInstanceState == null) {
            addFragments();
        }

        binding.swipeRefreshViewGoods.setColorSchemeResources(R.color.colorPrimary);

        viewModel.getGoodDetails();

        if (!TextUtils.isEmpty(viewModel.getType())) {
            String type = viewModel.getType();
            if (type.equals("1")) {//  推广id  1-专区推广
                viewModel.promotionCut();
            } else if (type.equals("2")) {//   2-搜索推广
                viewModel.promotionCutProductSearch();
            }
        }
        binding.swipeRefreshViewGoods.postDelayed(() -> {
            binding.swipeRefreshViewGoods.setRefreshing(true);
            for (OnRefreshListener listener : listeners) {
                listener.onRefresh();
            }
        }, 20);

        binding.swipeRefreshViewGoods.setOnRefreshListener(() -> {
            for (OnRefreshListener listener : listeners) {
                listener.onRefresh();
            }
        });

        observe();

        //id	Number	  【必填】当前访问的对象ID（医生ID/咨询师ID/医院ID/商品ID等）
        //type	Number	  【必填】访问对象类型，1/咨询师；2/医生；3/机构；4/商品。
        viewModel.addVisit(viewModel.getGoodId(), "4");
    }

    private void observe() {

        viewModel.goodDetailLive.observe(this, goodDetailBean -> {

            finishRefresh();
            binding.llGoodsBottom.setVisibility(View.VISIBLE);

            //是否是秒杀商品  0-不是，1-是
            // ①正常商品的秒杀 ②秒杀列表中的秒杀(已抢购完和未开始抢购 不可购买)
            if (goodDetailBean.getIs_killing() == 1) {
                long current_time = goodDetailBean.getCurrent_time() * 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(current_time);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int kill_time = goodDetailBean.getKill_time();

                //  不在抢购时间内
                if (hourOfDay < kill_time || hourOfDay >= kill_time + 2) {

                    if (AccountHelper.getIdentity() <= 1) {

                        binding.btnGroupBuying.setVisibility(View.VISIBLE);
                        binding.btnRushPurchase.setText(getString(R.string.snapped_up_immediately, goodDetailBean.getBuy_price()));
                        binding.btnRushPurchase.setBackgroundResource(R.color.f29c9f);
                    }

                } else {

                    binding.btnGroupBuying.setVisibility(View.GONE);
                    String text = getString(R.string.rmb_symbol) + goodDetailBean.getKill_price() + "   " + getString(R.string.snapped_up_immediately_seckill);
                    binding.btnRushPurchase.setText(text);
                    binding.btnRushPurchase.setBackgroundResource(R.color.colorPrimary);

                }

            } else {
                if (AccountHelper.getIdentity() <= 1) {

                    binding.btnGroupBuying.setVisibility(View.VISIBLE);
                    binding.btnRushPurchase.setText(getString(R.string.snapped_up_immediately, goodDetailBean.getBuy_price()));
                    binding.btnRushPurchase.setBackgroundResource(R.color.f29c9f);
                }
            }
        });

        viewModel.addToCarLive.observe(this, aBoolean -> {
            ToastUtils.showShort("添加购物车成功");
        });
    }

    private void addFragments() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction
                .add(R.id.fragment_container, GoodInfoFragment.newInstance(getChildFragmentManager()), GoodInfoFragment.class.getSimpleName())
                .add(R.id.fragment_container, SubSpellFragment.newInstance(getChildFragmentManager(),
                        viewModel.getGoodId()),
                        SubSpellFragment.class.getSimpleName())
                .add(R.id.fragment_container, BindedDoctorFragment.newInstance(getChildFragmentManager()), BindedDoctorFragment.class.getSimpleName())
                .add(R.id.fragment_container, SubEstimateFragment.newInstance(getChildFragmentManager(),
                        viewModel.getGoodId(), 1), SubEstimateFragment.class.getSimpleName())
                .add(R.id.fragment_container, SubPeopleSayFragment.newInstance(getChildFragmentManager(),
                        viewModel.getGoodId(), 1),
                        SubPeopleSayFragment.class.getSimpleName())
                .add(R.id.fragment_container, HotGoodsFragment.newInstance(getChildFragmentManager()), HotGoodsFragment.class.getSimpleName())
                .add(R.id.fragment_container, RelatedGoodsFragment.newInstance(getChildFragmentManager()), RelatedGoodsFragment.class.getSimpleName())
        ;

        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_org_homepage:
            case R.id.btn_org_homepage: //  机构主页
                GoodDetailBean detailBean = viewModel.goodDetailLive.getValue();
                if (detailBean != null && detailBean.getHospital_data() != null) {
                    HospitalBean hospital_data = detailBean.getHospital_data();
                    String dataId = hospital_data.getId();
                    HospitalHomePageActivity.start(getActivity(), dataId);
                }
                break;
            case R.id.btn_online_services:  //  在线咨询
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (AccountHelper.getIdentity() != 1) {
                    ToastUtils.showShort("只有普通用户可以咨询");
                    return;
                }
                ConsultantViewModel consultantViewModel = ViewModelProviders.of(getActivity()).get(ConsultantViewModel.class);
                consultantViewModel.consultantLive.observe(this, new Observer<ConsultantBean>() {
                    @Override
                    public void onChanged(@Nullable ConsultantBean consultantBean) {
                        consultantViewModel.consultantLive.removeObserver(this);
                        dismissLoading();
                        if (consultantBean == null) {
                            ToastUtils.showShort("没有可咨询的咨询师了");
                            return;
                        }
                        consultantViewModel.consultantLive.setValue(null);
                        ConsultantBean.ConsultantsAccidBean consultants_accid = consultantBean.getConsultants_accid();
                        if (consultants_accid == null || TextUtils.isEmpty(consultants_accid.getYunxin_accid())
                                || TextUtils.isEmpty(consultants_accid.getId())) {
                            ToastUtils.showShort("没有可咨询的咨询师了");
                            return;
                        }
                        String yunxin_accid = consultants_accid.getYunxin_accid();
                        String consultantId = consultants_accid.getId();
                        if (!TextUtils.isEmpty(yunxin_accid) && !TextUtils.isEmpty(consultantId)) {
                            NimUtils.startP2PSession(getActivity(), yunxin_accid, 1, viewModel.getGoodId(), consultantId);
                        }
                    }
                });
                consultantViewModel.setQueryId(viewModel.getGoodId());
                consultantViewModel.setGetConsultantsType("goods");
                consultantViewModel.getConsultant();

                break;
            case R.id.btn_add_shopcar:   //  添加到购物车
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (!identityValidate()) {
                    return;
                }
                AddToCarSelectDoctorDialog addToCarSelectDoctorDialog = new AddToCarSelectDoctorDialog();
                addToCarSelectDoctorDialog.show(getChildFragmentManager(), AddToCarSelectDoctorDialog.class.getSimpleName());
                viewModel.goodDetailLive.observe(addToCarSelectDoctorDialog, bean -> {
                    addToCarSelectDoctorDialog.setDoctors(bean.getDoctor_data());
                    addToCarSelectDoctorDialog.setCounselors(bean.getCounselling_data());
                    addToCarSelectDoctorDialog.setHospitalName(bean.getOrganization_name());
                });
                addToCarSelectDoctorDialog.setListener((doctorId, counselorId) -> {
                    viewModel.addToCart(doctorId, counselorId);
                });
                break;
            case R.id.btn_rush_purchase:  //  立即抢购

                if (AccountHelper.shouldLogin(getContext())) {
                    return;
                }
                if (!identityValidate()) {
                    return;
                }
//                if (!viewModel.isIs_buy()) {
//                    toast("已抢完或暂未开始抢购!");
//                    return;
//                }
                viewModel.goodDetailLive.observe(this, new Observer<GoodDetailBean>() {
                    @Override
                    public void onChanged(@Nullable GoodDetailBean bean) {
                        viewModel.goodDetailLive.removeObserver(this);
                        if (bean == null) {
                            return;
                        }
                        String promotionId = viewModel.getPromotionId();
                        boolean isAd = !TextUtils.isEmpty(promotionId) && !TextUtils.equals(promotionId, "0");

                        EventBus.getDefault().postSticky(bean);
                        if (bean.getIs_killing() == 1) {

                            long current_time = bean.getCurrent_time() * 1000;
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(current_time);
                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                            int kill_time = bean.getKill_time();
                            //  不在抢购时间内，是普通下单
                            if (hourOfDay < kill_time || hourOfDay >= kill_time + 2) {
                                ConfirmGoodsOrderActivity.start(getActivity(),
                                        ConfirmGoodsOrderActivity.ORDER_TYPE_NORMAL, isAd);
                            } else {
                                ConfirmGoodsOrderActivity.start(getActivity(),
                                        ConfirmGoodsOrderActivity.ORDER_TYPE_SECKILL, isAd);
                            }

                        } else {
                            ConfirmGoodsOrderActivity.start(getActivity(),
                                    ConfirmGoodsOrderActivity.ORDER_TYPE_NORMAL, isAd);
                        }
                    }
                });

                break;
            case R.id.btn_group_buying:  //  发起拼购
                if (AccountHelper.shouldLogin(getContext())) {
                    return;
                }
                if (!identityValidate()) {
                    return;
                }
//                if (!viewModel.isIs_buy()) {
//                    toast("已抢完或暂未开始抢购!");
//                    return;
//                }
                viewModel.goodDetailLive.observe(this, new Observer<GoodDetailBean>() {
                    @Override
                    public void onChanged(@Nullable GoodDetailBean bean) {
                        viewModel.goodDetailLive.removeObserver(this);
                        if (bean == null) {
                            return;
                        }

                        String promotionId = viewModel.getPromotionId();
                        boolean isAd = !TextUtils.isEmpty(promotionId) && !TextUtils.equals(promotionId, "0");

                        EventBus.getDefault().postSticky(bean);
                        ConfirmGoodsOrderActivity.start(getActivity(),
                                ConfirmGoodsOrderActivity.ORDER_TYPE_SPELL, isAd);
                    }
                });
                break;
        }
    }

    @Override
    public void register(OnRefreshListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unRegister(OnRefreshListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void finishRefresh() {
        boolean finished = true;
        for (OnRefreshListener listener : listeners) {
            finished &= listener.isRefreshFinished();
        }
        if (finished) {
            binding.swipeRefreshViewGoods.setRefreshing(false);
        }
    }

    private boolean identityValidate() {
        int identity = AccountHelper.getIdentity();
        if (identity == 1) {
            return true;
        }
        if (identity == 2) {
            ToastUtils.showShort("医生不能购买商品");
        } else if (identity == 3) {
            ToastUtils.showShort("咨询师不能购买商品");
        } else if (identity == 4) {
            ToastUtils.showShort("医院不能购买商品");
        }
        return false;
    }
}
