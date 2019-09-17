package com.chengzi.app.ui.peopleraise.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentRaiseDetailHeaderBinding;
import com.chengzi.app.databinding.ItemParticipateOrganizationLayoutBinding;
import com.chengzi.app.databinding.ItemParticipateUserLayoutBinding;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.peopleraise.bean.RaiseJoinOrgBean;
import com.chengzi.app.ui.peopleraise.bean.RaiseJoinUserBean;
import com.chengzi.app.ui.peopleraise.viewmodel.PeopleRaiseDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class RaiseDetailHeaderFragment extends BaseFragment<FragmentRaiseDetailHeaderBinding,
        PeopleRaiseDetailViewModel> {

    //    private static final String[] STATUS = {"待付款", "未达成", "待使用 ", "已完成", "已取消"};
    //1-进行中 2-已成功 4-未达成 -->详情中需要显示这3个状态，对应美人筹列表中的3个状态
    private static final String[] STATUS = {"待付款", "进行中", "已成功 ", "已完成", "未达成"};

    private ParticipateUserAdapter joinUserAdapter;
    private ParticipateOrgAdapter joinOrgAdapter;

    public RaiseDetailHeaderFragment() {
    }

    public static RaiseDetailHeaderFragment newInstance(FragmentManager fm) {
        RaiseDetailHeaderFragment fragment = (RaiseDetailHeaderFragment)
                fm.findFragmentByTag(RaiseDetailHeaderFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new RaiseDetailHeaderFragment();
            android.os.Bundle args = new Bundle();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_raise_detail_header;
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

        initParticipateRecycler();
        initParticipateOrgRecycler();

        obseve();

        viewModel.planDetails();

    }

    private void obseve() {

        //美人筹详情
        viewModel.raiseLive.observe(this, raiseBean -> {

            List<RaiseJoinUserBean> joinUser = raiseBean.getJoin_user();
            List<RaiseJoinOrgBean> joinOrg = raiseBean.getJoin_organization();
            joinUserAdapter.setNewData(joinUser);

            joinOrgAdapter.setNewData(joinOrg);
            if (joinOrgAdapter.getData().isEmpty()) {
                joinOrgAdapter.setEmptyView(R.layout.raise_join_org_empty_layout);
            }

            binding.setBean(raiseBean);

            int status = raiseBean.getStatus();
            if (status >= 0 && status < 5) {
                binding.tvStatus.setText(STATUS[status]);
            }
            //总人数 和 参与人数 -->普通用户是否可参与
            if (AccountHelper.getIdentity() == 1) {
                viewModel.setCount(raiseBean.getPeople_number(), joinUser.size());
            }
            //未达成
            if (status == 4) {
                viewModel.setTextView(false);
            }
            TextView tvAmount = getActivity().findViewById(R.id.tv_amount);
            tvAmount.setText(getString(R.string.price_place_holder_with_unit, raiseBean.getPrice()));

            SpannableStringBuilder priceBuilder = new SpannableStringBuilder();

            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(11, true);
            priceBuilder.append(getString(R.string.rmb_symbol), ass, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            ass = new AbsoluteSizeSpan(15, true);
            priceBuilder.append(raiseBean.getPrice(), ass, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ass = new AbsoluteSizeSpan(11, true);
            priceBuilder.append(getString(R.string.people_num_unit), ass, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            binding.tvRaisePrice.setText(priceBuilder);

            binding.raiseDetailHeaderRoot.setVisibility(View.VISIBLE);

        });


        viewModel.attentionLive.observe(this, pair -> {
            RaiseJoinUserBean bean = pair.first;
            Integer integer = pair.second;
            bean.setFollow(integer);
            viewModel.attentionObservable.update();
        });
    }


    private void initParticipateRecycler() {
        binding.recyclerParticipateUser.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        joinUserAdapter = new ParticipateUserAdapter();
        binding.recyclerParticipateUser.setAdapter(joinUserAdapter);
    }

    private void initParticipateOrgRecycler() {
        joinOrgAdapter = new ParticipateOrgAdapter();
        joinOrgAdapter.setHeaderAndEmpty(true);
        joinOrgAdapter.bindToRecyclerView(binding.recyclerParticipateOrganization);
    }


    @Subscribe
    public void onAttention(AttentionEvent event) {

        for (RaiseJoinUserBean bean : joinUserAdapter.getData()) {

            if (TextUtils.equals(bean.getUser_id(), event.getUserId())) {
                int integer = event.isAttention() ? 1 : 0;
                bean.setFollow(integer);
                viewModel.attentionObservable.update();
            }
        }
    }

    private class ParticipateUserAdapter extends BaseQuickAdapter<RaiseJoinUserBean, BaseViewHolder> implements ClickEventHandler<RaiseJoinUserBean> {

        public ParticipateUserAdapter() {
            super(R.layout.item_participate_user_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, RaiseJoinUserBean item) {
            ItemParticipateUserLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setIsSelf(TextUtils.equals(item.getUser_id(), AccountHelper.getUserId()));
            layoutBinding.setListener(this);
            layoutBinding.setAlwaysaLiveObservable(viewModel.attentionObservable);
            layoutBinding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, RaiseJoinUserBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            switch (view.getId()) {
                case R.id.btn_attention:
                    if (AccountHelper.shouldLogin(view.getContext())) {
                        return;
                    }
                    viewModel.follow(bean);
                    break;
                case R.id.head_image:
                case R.id.tv_nick_name:

                    UserHomePageActivity.start(view.getContext(), bean.getUser_id());
                    break;
            }
        }
    }


    private class ParticipateOrgAdapter extends BaseQuickAdapter<RaiseJoinOrgBean, BaseViewHolder> {

        public ParticipateOrgAdapter() {
            super(R.layout.item_participate_organization_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, RaiseJoinOrgBean item) {
            ItemParticipateOrganizationLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

}
