package com.chengzi.app.ui.vip.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindBaseAdapter;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSelectionVipBinding;
import com.chengzi.app.databinding.SelectionVipHeaderLayoutBinding;
import com.chengzi.app.event.OnCityChangeEvent;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.ui.vip.bean.VipUserInfoBean;
import com.chengzi.app.ui.vip.viewmodel.VipViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 精选vip
 *
 * @ClassName:SelectionVipFragment
 * @PackageName:com.yimei.app.ui.vip.fragment
 * @Create On 2019/4/13 0013   14:56
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class SelectionVipFragment extends BaseFragment<FragmentSelectionVipBinding, VipViewModel> {

    private FindBaseAdapter<GoodBean> baseAdapter;

    public SelectionVipFragment() {
    }

    public static SelectionVipFragment newInstance() {
        android.os.Bundle args = new Bundle();
        SelectionVipFragment fragment = new SelectionVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ///用户信息
    private SelectionVipHeaderLayoutBinding headerLayoutBinding;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_selection_vip;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);

        baseAdapter = new FindBaseAdapter();
        baseAdapter.setVip(true);
        baseAdapter.setHeaderAndEmpty(true);
        baseAdapter.setFactory(new FindBaseAdapter.ItemAdapterFactory<GoodBean>() {
            @Override
            public BaseQuickAdapter createAdapter(List<GoodBean> list) {
                MainGoodsAdapter adapter = new MainGoodsAdapter(1);
                adapter.setNewData(list);
                return adapter;
            }

            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });
        //用户信息
        headerLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.selection_vip_header_layout, binding.recyclerView, false);
        baseAdapter.addHeaderView(headerLayoutBinding.getRoot());
        headerLayoutBinding.setListener(clickListener);
        binding.recyclerView.setAdapter(baseAdapter);

        viewModel.getVipChannelHomepage();
        listenRefresh();
        observe();
        //登录显示
        if (AccountHelper.isLogin() && !TextUtils.isEmpty(AccountHelper.getUserId())) {
            viewModel.vipChannelIndex();
        }

    }

    //  切换城市后刷新
    @Subscribe
    public void onCityChange(OnCityChangeEvent event) {
        viewModel.getVipChannelHomepage();
    }

    private void observe() {
        viewModel.vipChannelHomeLive.observe(this, vipChannelHomePageBean -> {

            viewModel.refreshObservable.update();

            List<FindBaseBean<GoodBean>> category_list = vipChannelHomePageBean.getCategory_list();
            for (int i = 0; i < category_list.size(); i++) {
                if (category_list.get(i).getData().isEmpty()) {
                    category_list.remove(i);
                    i--;
                }
            }
            baseAdapter.setNewData(category_list);
        });
        //获取首页基本信息
        viewModel.vipChannelIndexLive.observe(this, vipChannelIndexBean -> {
            dismissLoading();
            if (vipChannelIndexBean != null && vipChannelIndexBean.getUser() != null) {
                VipUserInfoBean userinfo = vipChannelIndexBean.getUser();
                if (userinfo != null) {
                    show(userinfo);
                } else {
                    headerLayoutBinding.imageHead.setImageResource(R.drawable.morentouxiang);
                }
            }
        });
    }

    ///获取VIP信息->设置用户
    public void show(VipUserInfoBean bean) {
        headerLayoutBinding.setBean(bean);
        headerLayoutBinding.setUrl(bean.getHeadimg());
        headerLayoutBinding.executePendingBindings();
    }

    //  下拉刷新
    private void listenRefresh() {
        viewModel.alwaysAliveObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                if (!getUserVisibleHint()) {
                    return;
                }
                viewModel.getVipChannelHomepage();
                if (AccountHelper.isLogin() && !TextUtils.isEmpty(AccountHelper.getUserId())) {
                    viewModel.vipChannelIndex();
                }
            }
        });

    }

    private boolean isRefresh = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!AccountHelper.isLogin()) {
            VipUserInfoBean vipUserInfoBean = new VipUserInfoBean();
            vipUserInfoBean.setIs_vip(false);
            vipUserInfoBean.setVip_endtime("");
            headerLayoutBinding.setBean(vipUserInfoBean);
            headerLayoutBinding.imageHead.setImageResource(R.drawable.morentouxiang);
            headerLayoutBinding.executePendingBindings();
            return;
        }
        if (isRefresh) {
            if (AccountHelper.isLogin() && !TextUtils.isEmpty(AccountHelper.getUserId())) {
                viewModel.vipChannelIndex();
            }
        }
        isRefresh = false;
    }

    private ClickEventHandler<VipUserInfoBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_show_vip:  //查看介绍 -->当前登录身份已保存本地。不用传过去判断
                if (AccountHelper.shouldLogin(getActivity())) {
                    isRefresh = true;
                    return;
                }
//                goActivity(LookVipIntroduceActivity.class);
                //vip规则介绍：8-用户 9-咨询师 10-医生 11-医院
                String t_id = "";
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    t_id = "8";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                    t_id = "9";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                    t_id = "10";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                    t_id = "11";
                }
                LookVipIntroduceActivity.start(getContext(), t_id);
                break;
        }
    };
}