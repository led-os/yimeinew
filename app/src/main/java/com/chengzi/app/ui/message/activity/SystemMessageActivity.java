package com.chengzi.app.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivitySystemMessageBinding;
import com.chengzi.app.databinding.ItemSysMsgLayoutBinding;
import com.chengzi.app.ui.message.bean.SystemMsgBean;
import com.chengzi.app.ui.message.viewmodel.SystemMessageViewModel;

/**
 * 系统消息
 * @ClassName:SystemMessageActivity

 * @PackageName:com.yimei.app.ui.message.activity

 * @Create On 2019/4/11 0011   17:19

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class SystemMessageActivity extends BaseActivity<ActivitySystemMessageBinding, SystemMessageViewModel> {

    public static void start(Context context) {
        Intent intent = new Intent(context, SystemMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_system_message;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView,mViewModel);
        SysMsgAdapter adapter = new SysMsgAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this,LinearLayout.VERTICAL));

        mViewModel.liveData.observe(this,list -> {
            helper.onComplete(list);
        });
        helper.start();
    }

    private static class SysMsgAdapter extends BaseQuickAdapter<SystemMsgBean,BaseViewHolder>{

        public SysMsgAdapter() {
            super(R.layout.item_sys_msg_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, SystemMsgBean item) {
            ItemSysMsgLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }
}
