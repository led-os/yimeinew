package com.chengzi.app.ui.seckill.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySeckillBinding;
import com.chengzi.app.databinding.ItemSeckillLayoutBinding;
import com.chengzi.app.databinding.SearchTabViewBinding;
import com.chengzi.app.databinding.TabSeckillTimeLayoutBinding;
import com.chengzi.app.databinding.TabSeckillTimeLayoutBindingImpl;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.seckill.bean.KillIndexBean;
import com.chengzi.app.ui.seckill.bean.KillListBean;
import com.chengzi.app.ui.seckill.bean.KillTimeBean;
import com.chengzi.app.ui.seckill.viewmodel.SeckillViewModel;
import com.chengzi.app.widget.SeckillView;

import java.util.List;

/**
 * 限时秒杀
 *
 * @ClassName:SeckillActivity
 * @PackageName:com.yimei.app.ui.seckill.activity
 * @Create On 2019/4/19 0019   18:04
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class SeckillActivity extends BaseActivity<ActivitySeckillBinding, SeckillViewModel> {

    private PagingLoadHelper helper;
    //操作的pos
    private int pos = 0;
    private SeckillAdapter adapter;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_seckill;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        initRecycler();
        initTab();
        initTabTime();

        showLoading(Sys.LOADING);
        //分类
        mViewModel.categoryList();
        //秒杀时间端
        mViewModel.killTime();

        observe();
    }

    private void initRecycler() {
        adapter = new SeckillAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        mBinding.swipeRefreshView.setAdapter(adapter);
        ((SimpleItemAnimator) mBinding.swipeRefreshView.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);
        //item child点击事件
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {

            if (!ClickEvent.shouldClick(view)) {
                return;
            }

            pos = position;
            KillIndexBean bean = adapter.getData().get(position);
            if (AccountHelper.shouldLogin(this)) {
                return;
            }
            if (TextUtils.equals(mViewModel.status.get(), "1")) {    //抢购中
                if (TextUtils.equals(bean.getStatus(), "1")) {  // 0-抢完  1-进行中
                    GoodsDetailActivity.start(this, bean.getGoods_id());
                }
            } else {            //即将开抢-->0-未提醒  1-已开启
                if (TextUtils.equals(bean.getRemind(), "0")) {
                    //提醒我
                    showLoading(Sys.LOADING);
                    mViewModel.killRemind(bean.getId());
                }
            }
        });
        //商品详情
        adapter.setOnItemClickListener((adapter1, view, position) -> {

            if (!ClickEvent.shouldClick(view)) {
                return;
            }

            KillIndexBean bean = adapter.getData().get(position);
            /*boolean is_buy = true;
            if (mViewModel.status.get().equals("1")) {    //抢购中
                if (bean.getStatus().equals("0")) {  // 0-抢完  1-进行中
                    is_buy = false;
                }
            } else {            //即将开抢-->0-未提醒  1-已开启
                is_buy = false;
            }
            GoodsDetailActivity.start(this, bean.getGoods_id(), is_buy);*/
            GoodsDetailActivity.start(this, bean.getGoods_id());
        });
    }

    //更改分类/时间段tab刷新数据
    private void setHelp() {
        String category_id = mViewModel.category_id.get();
        String times = mViewModel.times.get();
        if (!TextUtils.isEmpty(category_id) && !TextUtils.isEmpty(times)) {
//            //停止倒计时
            mBinding.llInKill.setStopTimer();
            helper.start();
        }
    }

    //倒计时时间控件
    private void setTime(long times) {
        String text = TextUtils.equals(mViewModel.status.get(), "1") ? "距结束" : "距开始";
        mBinding.llInKill.setStopTimer();
        if (times != 0) {
            mBinding.llInKill.setRemainTime(text, times);
        }
        mBinding.llInKill.setListener(new SeckillView.OnSeckillFinishListener() {
            @Override
            public void onSeckillFinish() {
                toast("此时间段秒杀已结束，刷新新的时间");
//                String time = "0";
                mBinding.llInKill.setStopTimer();
                mViewModel.times.set(null); //等待刷新新的抢购中的时间
                mViewModel.status.set(null); //等待刷新新的抢购中的时间
                //刷新抢购时间
                mViewModel.killTime();
            }
        });
    }

    private void observe() {
        //分类
        mViewModel.categoryLive.observe(this, categoryBean -> {
//            List<CategoryItem> put = navigatorListBean.getPut();
            if (categoryBean == null || categoryBean.isEmpty()) {
                return;
            }
            mBinding.tabLayout.removeAllTabs();
            for (CategoryItem categoryItem : categoryBean) {
                SearchTabViewBinding tabViewBinding = DataBindingUtil.inflate(getLayoutInflater(),
                        R.layout.search_tab_view, mBinding.tabLayout, false);
                tabViewBinding.setBean(categoryItem);
                tabViewBinding.textView.setText(categoryItem.getTypeText());
                tabViewBinding.textView.setTextColor(Color.WHITE);
                tabViewBinding.executePendingBindings();
                CustomTabLayout.Tab tab = mBinding.tabLayout.newTab()
                        .setCustomView(tabViewBinding.getRoot());
                mBinding.tabLayout.addTab(tab);
            }
            //一级分类下的广告-->推荐
            mViewModel.category_id.set(categoryBean.get(0).getId());

            setHelp();
        });
        //秒杀时间段
        mViewModel.killTimeLive.observe(this, list -> {
            dismissLoading();
            if (list != null && list.size() > 0) {
                mBinding.tabLayoutTime.removeAllTabs();
                for (KillTimeBean bean : list) {
                    TabSeckillTimeLayoutBinding tabBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.tab_seckill_time_layout, mBinding.tabLayoutTime, false);
                    tabBinding.setBean(bean);
                    tabBinding.textView.setText(bean.getTime() + "\n" + bean.getStatus_Name());
                    tabBinding.executePendingBindings();
                    TabLayout.Tab tab = mBinding.tabLayoutTime.newTab().setCustomView(tabBinding.getRoot());
                    mBinding.tabLayoutTime.addTab(tab);
                }
                mViewModel.times.set(list.get(0).getNum());
                mViewModel.status.set(list.get(0).getStatus());
                setHelp();
            }
        });
        //抢购列表+倒计时
        mViewModel.killIndexLiveData.observe(this, killBean -> {
            if (killBean != null && killBean.getData() != null) {
                KillListBean data = killBean.getData();
                //订单列表
                if (data.getData() != null && data.getData().getData() != null && data.getData().getData() != null) {
                    List<KillIndexBean> data1 = data.getData().getData();
                    helper.onComplete(data1);
                } else {
                    helper.onComplete(null);
                }
                String time = "0";
                //开始/结束时间
                if (TextUtils.equals(mViewModel.status.get(), "1")) {   //进行中-->距结束
                    time = data.getEnd_time();
                } else {
                    time = data.getStart_time();
                }
                //时间不等于null 则进行倒计时
                setTime(Long.parseLong(time));
            } else {
                helper.onComplete(null);
                setTime(0);
            }
        });
        //提醒我
        mViewModel.killRemindLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                KillIndexBean killIndexBean = adapter.getData().get(pos);
                // // 0-未提醒  1-已开启  --->成功代表开启
                killIndexBean.setRemind("1");
                adapter.notifyItemChanged(pos);
            }
        });
    }

    //初始化分类
    private void initTab() {
        mBinding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                Object tag = tab.getCustomView().getTag(R.id.dataBinding);
                if (tag != null && (tag instanceof SearchTabViewBinding) && ((SearchTabViewBinding) tag).getBean() != null) {
                    CategoryItem bean = ((SearchTabViewBinding) tag).getBean();
                    String id = bean.getId();
                    //一级分类
                    mViewModel.category_id.set(id);
                    setHelp();
                }
            }
        });
    }

    //初始化时间段
    private void initTabTime() {
        mBinding.tabLayoutTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Object tag = tab.getCustomView().getTag(R.id.dataBinding);
                if (tag != null && (tag instanceof TabSeckillTimeLayoutBindingImpl) && ((TabSeckillTimeLayoutBindingImpl) tag).getBean() != null) {
                    KillTimeBean bean = ((TabSeckillTimeLayoutBindingImpl) tag).getBean();
                    String num = bean.getNum();
                    //一级分类
                    mViewModel.times.set(num);
                    mViewModel.status.set(bean.getStatus());
                    //切换倒计时
                    setTime(0);
                    setHelp();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class SeckillAdapter extends BaseQuickAdapter<KillIndexBean, BaseViewHolder> {

        public SeckillAdapter() {
            super(R.layout.item_seckill_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, KillIndexBean item) {
            ItemSeckillLayoutBinding itemBinding = DataBindingUtil.bind(helper.itemView);
            itemBinding.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            itemBinding.setBean(item);
            itemBinding.setUrl(item.getImage());
            itemBinding.setIsVip(TextUtils.equals(item.getIs_vip(), "1"));
            // //1-抢购中 0-即将开始
            itemBinding.setIsShowStatus(TextUtils.equals(mViewModel.status.get(), "1"));
            itemBinding.executePendingBindings();
            // 0-未提醒  1-已开启
            String remind = item.getRemind();
            // 0-抢完  1-进行中(立即抢购)
            String status = item.getStatus();
            if (TextUtils.equals(mViewModel.status.get(), "1")) {    //抢购中
                itemBinding.tvGo.setText(TextUtils.equals(status, "1") ? "立即抢购" : "已抢完");
                itemBinding.tvGo.setEnabled(TextUtils.equals(status, "1") ? true : false);
            } else {            //即将开抢
                itemBinding.tvGo.setText(TextUtils.equals(remind, "1") ? "已开启" : "提醒我");
                itemBinding.tvGo.setEnabled(TextUtils.equals(remind, "1") ? false : true);

            }
            helper.addOnClickListener(R.id.tv_go);
        }
    }
}