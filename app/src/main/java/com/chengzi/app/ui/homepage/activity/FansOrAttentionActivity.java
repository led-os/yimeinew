package com.chengzi.app.ui.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityFansOrAttentionBinding;
import com.chengzi.app.databinding.ItemFanOrAttentionLayoutBinding;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.homepage.viewmodel.HisFansViewModel;
import com.chengzi.app.ui.mine.bean.FollowBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 医生/咨询师/医院  的粉丝
 *
 * @ClassName:HisFansActivity
 * @PackageName:com.yimei.app.ui.homepage.doctor.activity
 * @Create On 2019/4/18 0018   17:26
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class FansOrAttentionActivity extends BaseActivity<ActivityFansOrAttentionBinding, HisFansViewModel> {

    private FansOrAttentionAdapter adapter;
    private PagingLoadHelper helper;

    public static void start(Context context, int type, String targetId) {
        Intent intent = new Intent(context, FansOrAttentionActivity.class);
        intent.putExtra(Sys.EXTRA_FANS_TYPE, type);
        intent.putExtra(Sys.EXTRA_TARGET_ID, targetId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_fans_or_attention;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();
        EventBus.getDefault().register(this);
        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        initRecycler();

        observe();
        helper.start();

    }

    private void observe() {
        //列表
        mViewModel.followListLiveData.observe(this, list -> {
            helper.onComplete(list);
        });

        //关注
        mViewModel.findFollowLive.observe(this, pair -> {
            FollowBean bean = pair.first;
            int position = pair.second;
            ToastUtils.showShort(bean.getIs_followed() == 1 ? "关注成功" : "已取消关注");
            adapter.notifyItemChanged(adapter.getHeaderLayoutCount() + position);
            EventBus.getDefault().post(new AttentionEvent(bean.getUser_id(), bean.getIs_followed() == 1));

            if (mViewModel.getType() == 1 && bean.getIs_followed() == 0) {
                helper.onPulldown();
            }
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        int types = intent.getIntExtra(Sys.EXTRA_FANS_TYPE, Sys.EXTRA_FANS);
        String targetId = intent.getStringExtra(Sys.EXTRA_TARGET_ID);
        mViewModel.setTargetId(targetId);

        mBinding.topBar.setCenterText(types == Sys.EXTRA_ATTENTION ?
                getString(R.string.attention) : getString(R.string.fans));
        mViewModel.setType(types == Sys.EXTRA_ATTENTION ? 1 : 2);
    }

    @Subscribe
    public void onAttention(AttentionEvent event) {

        if (mViewModel.getType() == 1 && !event.isAttention()) {
            helper.onPulldown();
            return;
        }

        for (int i = 0; i < adapter.getData().size(); i++) {
            FollowBean followBean = adapter.getData().get(i);
            if (TextUtils.equals(followBean.getUser_id(), event.getUserId())) {
                int integer = event.isAttention() ? 1 : 0;
                followBean.setIs_followed(integer);
                adapter.notifyItemChanged(adapter.getHeaderLayoutCount() + i);
                break;
            }
        }
    }


    private void initRecycler() {
        adapter = new FansOrAttentionAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        mBinding.swipeRefreshView.setAdapter(adapter);
        ((SimpleItemAnimator) mBinding.swipeRefreshView.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);

        //点击关注
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            if (AccountHelper.shouldLogin(this)) {
                return;
            }
            FollowBean followListBean = adapter.getData().get(position);
            mViewModel.findFollow(followListBean, position);

        });
        //点击进主页
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            FollowBean followListBean = adapter.getData().get(position);
            String user_id = followListBean.getUser_id();
            String type = followListBean.getUser_type() + "";
            // is_followed; // 0-未关注 1-已关注 2-自己
            boolean is_mine = TextUtils.equals(user_id, AccountHelper.getUserId());
            if (TextUtils.equals(type, "1")) {
                if (!is_mine) {
                    UserHomePageActivity.start(this, user_id);
                } else {
                    UserHomePageActivity.startSelf(this);
                }
            } else if (TextUtils.equals(type, "2")) {
                if (!is_mine) {
                    DoctorHomePageActivity.startDoctor(this, user_id);
                } else {
                    DoctorHomePageActivity.startDoctorSelt(this);
                }
            } else if (TextUtils.equals(type, "3")) {
                if (!is_mine) {
                    DoctorHomePageActivity.startCounselor(this, user_id);
                } else {
                    DoctorHomePageActivity.startCounselorSelt(this);
                }
            } else if (TextUtils.equals(type, "4")) {
                if (!is_mine) {
                    HospitalHomePageActivity.start(this, user_id);
                } else {
                    HospitalHomePageActivity.startSelf(this);
                }
            }
        });
    }

    private static class FansOrAttentionAdapter extends BaseQuickAdapter<FollowBean, BaseViewHolder> {

        public FansOrAttentionAdapter() {
            super(R.layout.item_fan_or_attention_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, FollowBean item) {
            ItemFanOrAttentionLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setIsSelf(TextUtils.equals(item.getUser_id(), AccountHelper.getUserId()));
            layoutBinding.executePendingBindings();
            helper.addOnClickListener(R.id.tv_followed);
        }
    }
}