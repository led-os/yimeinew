package com.chengzi.app.ui.privaterefer.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPrivateReferTypeBinding;
import com.chengzi.app.databinding.ItemPrivateReferTypeLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.mine.activity.vip.OpenVipActivity;
import com.chengzi.app.ui.privaterefer.viewmodel.PrivateReferTypeViewModel;
import com.chengzi.app.ui.privaterefer.viewmodel.PrivateReferViewModel;

/**
 * 私享咨询/在线面诊--选择类型
 *
 * @ClassName:PrivateReferTypeActivity
 * @PackageName:com.yimei.app.ui.privaterefer.activity
 * @Create On 2019/4/9 0009   10:02
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
public class PrivateReferTypeActivity extends BaseActivity<ActivityPrivateReferTypeBinding,
        PrivateReferTypeViewModel> implements View.OnClickListener {


    private TypeAdapter adapter;

    public static void start(Context context, int type) {
        BaseActivity activity = (BaseActivity) context;
        if (AccountHelper.shouldLogin(activity)) {
            return;
        }
        if (AccountHelper.getIdentity() > 1) {
            ToastUtils.showShort("只有普通用户可以私享咨询");
            return;
        }
        activity.showLoading("");
        PrivateReferViewModel viewModel = ViewModelProviders.of(activity).get(PrivateReferViewModel.class);
        viewModel.userInfoLiveData.observeForever(new Observer<UserInfoBean>() {
            @Override
            public void onChanged(@Nullable UserInfoBean infoBean) {
                if (infoBean == null) {
                    return;
                }
                viewModel.userInfoLiveData.setValue(null);
                viewModel.userInfoLiveData.removeObserver(this);
                activity.dismissLoading();
                if (infoBean == null) {
                    return;
                }
                int is_vip = infoBean.getIs_VIP();
                if (is_vip == 1) {
                    Intent intent = new Intent(context, PrivateReferTypeActivity.class);
                    intent.putExtra(Sys.EXTRA, type);
                    context.startActivity(intent);
                } else {
                    new MessageDialogBuilder(activity)
                            .setMessage("你还不是VIP，是否去购买？")
                            .setSureListener(v -> OpenVipActivity.start(activity))
                            .show();
                }
            }
        });
        viewModel.userInfo();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_private_refer_type;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);

        parseIntent();

        initRecycler();

        mViewModel.homeCategoryLive.observe(this, categoryItems -> {
            if (categoryItems.size() > 0) {
                categoryItems.remove(0);
            }
            adapter.setNewData(categoryItems);
        });
        mViewModel.referLive.observe(this, referId -> {
            dismissLoading();
            MatchedActivity.start(this,mViewModel.getType(),referId);
//            WaitForMatchingActivity.start(this, mViewModel.getType(), referId);
        });

        mViewModel.homeCategory();

    }

    private void parseIntent() {
        int type = getIntent().getIntExtra(Sys.EXTRA, Sys.TYPE_PRIVATE_REFER);
        if (type == Sys.TYPE_DIAGNOSE_ONLINE) {
            mBinding.topBar.setCenterText(getString(R.string.online_consultation));
        } else {
            mBinding.topBar.setCenterText(getString(R.string.consult));
        }
        mViewModel.setType(type);
    }

    private void initRecycler() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new TypeAdapter();
        mBinding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

            if (AccountHelper.shouldLogin(this)) {
                return;
            }

            CategoryItem item = adapter.getItem(position);
            showLoading("");
            mViewModel.referStart(item.getId());
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                onBackPressed();
                break;
        }
    }

    private class TypeAdapter extends BaseQuickAdapter<CategoryItem, BaseViewHolder> {

        public TypeAdapter() {
            super(R.layout.item_private_refer_type_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CategoryItem item) {
            ItemPrivateReferTypeLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.textView.setText(item.getName());
            binding.executePendingBindings();
        }
    }
}
