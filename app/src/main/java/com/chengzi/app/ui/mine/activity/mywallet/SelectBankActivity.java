package com.chengzi.app.ui.mine.activity.mywallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivitySelectBankBinding;
import com.chengzi.app.databinding.ItemBankLayoutBinding;
import com.chengzi.app.ui.mine.bean.BankBean;
import com.chengzi.app.ui.mine.viewmodel.BindCorporateViewModel;
import com.handong.framework.base.BaseActivity;

public class SelectBankActivity extends BaseActivity<ActivitySelectBankBinding, BindCorporateViewModel> {

    public static void start(Context context) {
        Intent intent = new Intent(context, SelectBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_select_bank;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        BankAdapter adapter = new BankAdapter();
        mBinding.recyclerView.setAdapter(adapter);
        mViewModel.getBanks();
        mViewModel.bankLive.observe(this,bankBeans -> {
            adapter.setNewData(bankBeans);
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            BankBean item = adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("bankName",item.getBank_name());
            intent.putExtra("bankId",item.getId());
            setResult(Activity.RESULT_OK,intent);
            finish();
        });
    }

    private class BankAdapter extends BaseQuickAdapter<BankBean, BaseViewHolder> {


        public BankAdapter() {
            super(R.layout.item_bank_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankBean item) {
             ItemBankLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
             layoutBinding.setBankName(item.getBank_name());
        }
    }

}
