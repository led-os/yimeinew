package com.chengzi.app.ui.mine.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentAccountDeailsBindingImpl;
import com.chengzi.app.ui.mine.bean.AccountFlowBean;
import com.chengzi.app.ui.mine.viewmodel.AccountDetailsViewModel;

/**
 * 账户明细 -->支付0/补给金1
 *
 * @ClassName:AccountDeailsFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/1 0001   15:53
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class AccountDeailsFragment extends LazyloadFragment<FragmentAccountDeailsBindingImpl, AccountDetailsViewModel> {
    private int position = 0;
    private PagingLoadHelper helper;

    public static AccountDeailsFragment newInstance(int position) {
        AccountDeailsFragment fragment = new AccountDeailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account_deails;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position", 0);
            viewModel.type.set(position == 0 ? String.valueOf(1) : String.valueOf(2));//1提现，2补给
        }
        AccountDeailsAdapter adapter = new AccountDeailsAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
//        binding.swipeRefreshView.addItemDecoration(new GridDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.accountLive.observe(this, objects -> {
            helper.onComplete(objects);
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    public class AccountDeailsAdapter extends BaseQuickAdapter<AccountFlowBean.DataBean, BaseViewHolder> {
        public AccountDeailsAdapter() {
            super(R.layout.item_account_deails_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, AccountFlowBean.DataBean item) {
            if (item.getChange_type() == 1) {
                //提现
                helper.setText(R.id.tv_name_type, "提现")
                        .setText(R.id.tv_time, item.getCreate_time())
                        .setText(R.id.tv_money, "-" + item.getMoney())
                        .setTextColor(R.id.tv_money, Color.parseColor("#FF526A"));
            } else {
                //补给金
                helper.setText(R.id.tv_name_type, "补给金")
                        .setText(R.id.tv_time, item.getCreate_time())
                        .setText(R.id.tv_money, "+" + item.getMoney())
                        .setTextColor(R.id.tv_money, Color.parseColor("#2DBA8F"));
                ;
            }
        }
    }
}