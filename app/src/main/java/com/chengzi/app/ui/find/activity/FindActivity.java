package com.chengzi.app.ui.find.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityFindBinding;
import com.chengzi.app.databinding.ItemFindLayoutBinding;
import com.chengzi.app.event.SwitchMainTabEvent;
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
 * 找
 *
 * @ClassName:LookForActivity
 * @PackageName:com.yimei.app.ui.lookfor.activity
 * @Create On 2019/4/4 0004   09:43
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class FindActivity extends BaseActivity<ActivityFindBinding, AppNavigatorViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_find;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        initTopicRecycler();
        initThridRecycler();

        observe();

    }

    private void observe(){
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

    private void initTopicRecycler() {
        MyAdapter adapter = new MyAdapter();
        mBinding.recyclerTopic.setAdapter(adapter);
        adapter.setNewData(generateData());
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Item item = adapter.getItem(position);
            if (item.clazz != null) {

                if (item.clazz == PrivateReferTypeActivity.class) {  //  在线面诊

                    PrivateReferTypeActivity.start(this, Sys.TYPE_DIAGNOSE_ONLINE);

                } else if (item.clazz == PrivateReferActivity.class) {

                    PrivateReferActivity.start(this);

                } else if (item.clazz == MainActivity.class) {  //  vip

                    finish();
                    EventBus.getDefault().post(new SwitchMainTabEvent(2));
//                    RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 2);

                } else if (item.clazz == CreditEnquiryActivity.class) {

                    if (AccountHelper.shouldLogin(this)) {
                        return;
                    }
                    startActivity(new Intent(this, item.clazz));
                    finish();
//                    if (AccountHelper.getIdentity() == 1) {
//                        startActivity(new Intent(this, item.clazz));
//                        finish();
//                    } else {
//                        toast("仅对普通用户开放");
//                    }
                } else if (item.clazz == Object.class) {

                    if (AccountHelper.shouldLogin(this)) {
                        return;
                    }
                    mViewModel.getCustomerServiceStaff();

                } else {
                    Intent intent = new Intent(this, item.clazz);
                    intent.putExtra("extra", item.extra);
                    startActivity(intent);
                }

            }
        });
    }

    private List<Item> generateData() {

        List<Item> list = new ArrayList<>();
        list.add(new Item(getString(R.string.lookfor_doctor), FindDetailActivity.class, FindDetailActivity.FIND_DOCTOR));
        list.add(new Item(getString(R.string.lookfor_good), FindDetailActivity.class, FindDetailActivity.FIND_GOODS));
        list.add(new Item(getString(R.string.lookfor_org), FindDetailActivity.class, FindDetailActivity.FIND_ORG));
        list.add(new Item(getString(R.string.lookfor_counsoler), FindDetailActivity.class, FindDetailActivity.FIND_COUNSOLER));
        list.add(new Item(getString(R.string.lookfor_case), FindDetailActivity.class, FindDetailActivity.FIND_CASE));
        list.add(new Item(getString(R.string.lookfor_answer_and_question), OnlineQuestionActivity.class));
        list.add(new Item(getString(R.string.consult), PrivateReferActivity.class));
        list.add(new Item(getString(R.string.online_consultation), PrivateReferTypeActivity.class));
        list.add(new Item(getString(R.string.beauty_raise), PeopleRaiseActivity.class));
        list.add(new Item(getString(R.string.ranking_list), RankingListActivity.class));
        list.add(new Item(getString(R.string.vip), MainActivity.class, 2));
        list.add(new Item(getString(R.string.credit_enquiry), CreditEnquiryActivity.class));
        list.add(new Item(getString(R.string.help_and_rule), HelpActivity.class));
        list.add(new Item(getString(R.string.suggestion_feedback), FeedBackActivity.class));

        if (AccountHelper.getIdentity() != 4) {
            list.add(new Item(getString(R.string.contact_us), Object.class));
        }

        return list;
    }

    private void initThridRecycler() {
        MyAdapter adapter = new MyAdapter();
        mBinding.recyclerThird.setAdapter(adapter);
        List<Item> list = new ArrayList<>();
        list.add(new Item(getString(R.string.hiring), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_RECRUITMENT));
        list.add(new Item(getString(R.string.industry_conference_release), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_MEETING));
        list.add(new Item(getString(R.string.training_to_release), ThreePartiesActivity.class, ThreePartiesActivity.THREEPARTIES_TRAINING));
        adapter.setNewData(list);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Item item = adapter.getItem(position);
            if (item.clazz != null) {
                Intent intent = new Intent(this, item.clazz);
                intent.putExtra("extra", item.extra);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseQuickAdapter<Item, BaseViewHolder> {

        public MyAdapter() {
            super(R.layout.item_find_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, Item item) {
            ItemFindLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.textView.setText(item.text);
        }
    }

    private static class Item {
        String text;
        Class clazz;
        int extra;

        public Item(String text, Class clazz, int extra) {
            this.text = text;
            this.clazz = clazz;
            this.extra = extra;
        }

        public Item(String text, Class clazz) {
            this.text = text;
            this.clazz = clazz;
        }
    }
}
