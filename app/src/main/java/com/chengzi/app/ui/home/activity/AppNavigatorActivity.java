package com.chengzi.app.ui.home.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Pair;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAppNavigatorBinding;
import com.chengzi.app.databinding.ItemAppNavigatorHeaderLayoutBinding;
import com.chengzi.app.databinding.ItemAppNavigatorLayoutBinding;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.find.activity.CreditEnquiryActivity;
import com.chengzi.app.ui.find.activity.FindDetailActivity;
import com.chengzi.app.ui.find.activity.ThreePartiesActivity;
import com.chengzi.app.ui.home.Navigator;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.GetCustomerServiceStaffBean;
import com.chengzi.app.ui.home.viewmodel.AppNavigatorViewModel;
import com.chengzi.app.ui.mine.activity.FeedBackActivity;
import com.chengzi.app.ui.mine.activity.HelpActivity;
import com.chengzi.app.ui.onlinequestionandanswer.activity.OnlineQuestionActivity;
import com.chengzi.app.ui.peopleraise.activity.PeopleRaiseActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferTypeActivity;
import com.chengzi.app.ui.rankinglist.activity.RankingListActivity;
import com.chengzi.app.utils.NimUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * app 导航
 *
 * @ClassName:AppNavigatorActivity
 * @PackageName:com.yimei.app.ui.home.activity
 * @Create On 2019/4/3 0003   17:20
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class AppNavigatorActivity extends BaseActivity<ActivityAppNavigatorBinding, AppNavigatorViewModel> {

    private AppNavigatorAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_app_navigator;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        initRecycler();

        observe();

        mViewModel.homeCategory();
        mViewModel.getVipCategory();

    }

    private void observe() {

        mViewModel.status.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Pair<Integer, Integer> pair = mViewModel.status.get();
                if (pair != null && pair.first == 1 && pair.second == 1) {
                    mBinding.swipeRefreshView.setRefreshing(false);

                    List<CategoryItem> normalCategories = mViewModel.homeCategoryLive.getValue();
                    List<CategoryItem> vipCategories = mViewModel.vipCategoryLive.getValue();

                    List<Navigator> list = new ArrayList<>();
                    list.addAll(findList());

                    if (normalCategories != null) {
                        list.add(new AppNavigatorItem(1, "普通专区"));
                        for (CategoryItem normalCategory : normalCategories) {
                            normalCategory.setIs_operation(0);
                        }
                        list.addAll(normalCategories);
                    }

                    if (vipCategories != null) {
                        list.add(new AppNavigatorItem(1, "VIP专区"));
                        for (CategoryItem vipCategory : vipCategories) {
                            vipCategory.setIs_operation(1);
                        }
                        list.addAll(vipCategories);
                    }

                    adapter.setNewData(list);
                }
            }
        });
        mViewModel.customerServiceLive.observe(this, serviceStaffBean -> {
            if (serviceStaffBean == null) {
                ToastUtils.showShort("没有客服人员");
                return;
            }
            GetCustomerServiceStaffBean.CustomerServiceStaffAccidBean customer = serviceStaffBean.getCustomer();
            if (customer == null || TextUtils.isEmpty(customer.getYunxin_accid())) {
                ToastUtils.showShort("没有客服人员");
                return;
            }
            String yunxin_accid = customer.getYunxin_accid();
            NimUtils.startP2PSession(this, yunxin_accid);
        });

    }

    private void initRecycler() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);

        mBinding.swipeRefreshView.setLayoutManager(manager);
        adapter = new AppNavigatorAdapter();
        adapter.setSpanSizeLookup((GridLayoutManager gridLayoutManager, int position) -> {
            int spanCount = gridLayoutManager.getSpanCount();
            return adapter.getItem(position).getItemType() == 1 ? spanCount : 1;
        });

        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setPullupEnable(false);
        mBinding.swipeRefreshView.setOnRefreshListener(() -> {
            mViewModel.status.set(new Pair<>(0, 0));
            mViewModel.homeCategory();
            mViewModel.getVipCategory();

        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Navigator item = adapter.getItem(position);
            if (item instanceof AppNavigatorItem) {
                AppNavigatorItem navigatorItem = (AppNavigatorItem) item;
                if (navigatorItem.clazz == PrivateReferActivity.class) {
                    PrivateReferActivity.start(this);
                } else if (navigatorItem.clazz == PrivateReferTypeActivity.class) {  //  在线面诊
                    PrivateReferTypeActivity.start(this, Sys.TYPE_DIAGNOSE_ONLINE);
                } else if (navigatorItem.clazz == MainActivity.class) {  //  vip
                    finish();
                    EventBus.getDefault().post(new SwitchMainTabEvent(2));
//                    RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 2);
                } else if (navigatorItem.clazz == Object.class) {
                    if (AccountHelper.shouldLogin(this)) {
                        return;
                    }
                    mViewModel.getCustomerServiceStaff();
                } else {
                    if (navigatorItem.clazz != null) {

                        Intent intent = new Intent(this, navigatorItem.clazz);
                        intent.putExtra("extra", navigatorItem.extra);
                        startActivity(intent);
                    }
                }
            } else if (item instanceof CategoryItem) {
                CategoryItem categoryItem = (CategoryItem) item;

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("categoryId", categoryItem.getId());

                if (categoryItem.getIs_operation() == 1) { //  vip 分类

                    intent.putExtra(Sys.EXTRA, 2);

                } else {    //  普通分类
                    intent.putExtra(Sys.EXTRA, 0);
                }
                startActivity(intent);
            }
        });

    }

    private List<AppNavigatorItem> findList() {
        List<AppNavigatorItem> list = new ArrayList<>();
        list.add(new AppNavigatorItem(1, "找"));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_doctor), FindDetailActivity.class, FindDetailActivity.FIND_DOCTOR));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_good), FindDetailActivity.class, FindDetailActivity.FIND_GOODS));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_org), FindDetailActivity.class, FindDetailActivity.FIND_ORG));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_counsoler), FindDetailActivity.class, FindDetailActivity.FIND_COUNSOLER));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_case), FindDetailActivity.class, FindDetailActivity.FIND_CASE));
        list.add(new AppNavigatorItem(0, getString(R.string.lookfor_answer_and_question), OnlineQuestionActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.consult), PrivateReferActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.online_consultation), PrivateReferTypeActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.beauty_raise), PeopleRaiseActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.ranking_list), RankingListActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.vip), MainActivity.class, 2));
        list.add(new AppNavigatorItem(0, getString(R.string.credit_enquiry), CreditEnquiryActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.help_and_rule), HelpActivity.class));
        list.add(new AppNavigatorItem(0, getString(R.string.suggestion_feedback), FeedBackActivity.class));

        if (AccountHelper.getIdentity() != 4) {
            list.add(new AppNavigatorItem(0, getString(R.string.contact_us), Object.class));
        }

        list.add(new AppNavigatorItem(1, "三方"));
        list.add(new AppNavigatorItem(0, getString(R.string.hiring), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_RECRUITMENT));
        list.add(new AppNavigatorItem(0, getString(R.string.industry_conference_release), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_MEETING));
        list.add(new AppNavigatorItem(0, getString(R.string.training_to_release), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_TRAINING));

        return list;
    }

    private class AppNavigatorAdapter extends BaseMultiItemQuickAdapter<Navigator, BaseViewHolder> {


        public AppNavigatorAdapter() {
            super(null);
            addItemType(1, R.layout.item_app_navigator_header_layout);
            addItemType(0, R.layout.item_app_navigator_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, Navigator item) {
            int itemViewType = helper.getItemViewType();
            if (itemViewType == 1) {
                ItemAppNavigatorHeaderLayoutBinding headerLayoutBinding = DataBindingUtil.bind(helper.itemView);
                headerLayoutBinding.setBean(item.getTypeText());
                headerLayoutBinding.executePendingBindings();
            } else {
                ItemAppNavigatorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
                layoutBinding.setBean(item.getTypeText());
                layoutBinding.executePendingBindings();
            }
        }
    }

    public static class AppNavigatorItem implements Navigator, MultiItemEntity {
        private int type;
        private String typeText;
        private Class clazz;
        private int extra;

        public AppNavigatorItem(int type, String typeText) {
            this.type = type;
            this.typeText = typeText;
        }

        public AppNavigatorItem(int type, String typeText, Class clazz) {
            this.type = type;
            this.typeText = typeText;
            this.clazz = clazz;
        }

        public AppNavigatorItem(int type, String typeText, Class clazz, int extra) {
            this.type = type;
            this.typeText = typeText;
            this.clazz = clazz;
            this.extra = extra;
        }

        @Override
        public int getItemType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }

        @Override
        public String toString() {
            return typeText;
        }
    }

}
