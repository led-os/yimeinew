package com.chengzi.app.ui.mine.activity.vip;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySendVipBindingImpl;
import com.chengzi.app.databinding.ItemSendVipLayoutBinding;
import com.chengzi.app.ui.mine.bean.VipGivingListBean;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;
import com.chengzi.app.utils.CommonUtilis;

import java.util.List;

/**
 * 转增VIP -->只支持普通用户
 *
 * @ClassName:SendVipActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/2 0002   15:29
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class SendVipActivity extends BaseActivity<ActivitySendVipBindingImpl, MyVipViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_send_vip;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setModel(mViewModel);
        mBinding.mRecyclerView.setVisibility(View.VISIBLE);
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        SendVipAdapter adapter = new SendVipAdapter();
        mBinding.mRecyclerView.setAdapter(adapter);
        //赠送好友列表
        showLoading(Sys.LOADING);
        mViewModel.vipGivingList();
        mViewModel.vipGivingListLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                mBinding.mRecyclerView.setVisibility(View.VISIBLE);
                mBinding.emptyView.setVisibility(View.GONE);
                List<VipGivingListBean> data = listResponseBean.getData();
                adapter.setNewData(data);
            } else {
                mBinding.mRecyclerView.setVisibility(View.GONE);
                mBinding.emptyView.setVisibility(View.VISIBLE);
            }
        });
        //确认赠送
        mViewModel.vipGivingLiveData.observe(this, responseBean -> {
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("赠送成功!");
                mViewModel.vipGivingList();
                //清空手机号
                mBinding.etPhone.setText("");
                //软键盘隐藏
                CommonUtilis.hideSoftKeyboard(this);
            } else {
                dismissLoading();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_vip_give:  //  确认赠送
                String mobile = mViewModel.mobile.get();
                ContentBody mobileBody = new ContentBody("好友注册手机号", mBinding.etPhone.getText().toString().trim());
                boolean checkResult = ContentChecker.getCheckMachine()
                        .putChecker(ContentChecker.getChecker(mobileBody)
                                .addCondition(new NotEmptyCondition(this))
                                .addCondition(new PhoneNumCondition()))
                        .checkAll();
                if (checkResult) {
                    if (!TextUtils.isEmpty(mobile)) {
                        showLoading(Sys.LOADING);
                        mViewModel.vipGiving();
                    } else {
                        toast("请输入好友注册手机号");
                    }
                }
                break;
        }
    };

    public class SendVipAdapter extends BaseQuickAdapter<VipGivingListBean, BaseViewHolder> {
        public SendVipAdapter() {
            super(R.layout.item_send_vip_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, VipGivingListBean item) {
            ItemSendVipLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setUrl(item.getReceiveimage());
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }
}