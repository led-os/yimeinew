package com.chengzi.app.ui.message.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityFansBinding;
import com.chengzi.app.databinding.ItemFansMsgLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.message.bean.FansMsgBean;
import com.chengzi.app.ui.message.viewmodel.FansViewModel;

/**
 * 粉丝消息
 *
 * @ClassName:FansActivity
 * @PackageName:com.yimei.app.ui.message.activity
 * @Create On 2019/4/11 0011   17:18
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class FansActivity extends BaseActivity<ActivityFansBinding, FansViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_fans;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        mBinding.swipeRefreshView.setAdapter(new FansMsgAdapter());
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });
        helper.start();

    }

    private class FansMsgAdapter extends BaseQuickAdapter<FansMsgBean, BaseViewHolder> implements ClickEventHandler<FansMsgBean> {

        public FansMsgAdapter() {
            super(R.layout.item_fans_msg_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, FansMsgBean item) {
            ItemFansMsgLayoutBinding layoutBinding =  DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setListener(this);
        }

        @Override
        public void handleClick(View view, FansMsgBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            switch (view.getId()) {
                case R.id.root:
                    int userType = bean.getUser_type();
                    if (userType == 1) {
                        if (view.getContext().getClass() == UserHomePageActivity.class) {
                            return;
                        }
                        UserHomePageActivity.start(view.getContext(), bean.getId());
                    } else if (userType == 2) {
                        DoctorHomePageActivity.startDoctor(view.getContext(), bean.getId());
                    } else if (userType == 3) {
                        DoctorHomePageActivity.startCounselor(view.getContext(), bean.getId());
                    } else {
                        HospitalHomePageActivity.start(view.getContext(), bean.getId());
                    }
                    break;
            }
        }
    }

}
