package com.chengzi.app.ui.privaterefer.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMatchedBinding;
import com.chengzi.app.databinding.ItemMainNormalLayoutBinding;
import com.chengzi.app.databinding.ItemMatchedDoctorLayoutBinding;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.privaterefer.ReferObservable;
import com.chengzi.app.ui.privaterefer.viewmodel.WaitForMatchViewModel;
import com.chengzi.app.utils.NimUtils;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 已匹配，已抢单
 *
 * @ClassName:MatchedActivity
 * @PackageName:com.yimei.app.ui.privaterefer.activity
 * @Create On 2019/4/9 0009   11:31
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
@RuntimePermissions
public class MatchedActivity extends BaseActivity<ActivityMatchedBinding, WaitForMatchViewModel>
        implements View.OnClickListener {

    private static final String EXTRA_REFER_ID = "extra_refer_id";
    private MatchedAdapter adapter;

    /**
     * @param context
     * @param type    0 私享咨询   1  在线诊断
     * @param referId 私享咨询 或 在线诊断的 id
     */
    public static void start(Context context, int type, String referId) {
        Intent intent = new Intent(context, MatchedActivity.class);
        intent.putExtra(Sys.EXTRA, type);
        intent.putExtra(EXTRA_REFER_ID, referId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_matched;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);

        parseIntent();
        initRecyclerView();
        observe();

        if (savedInstanceState == null) {

            //  每 1秒发一次请求，查询是否有医生/咨询师接单
            ReferObservable.interval(this, () -> {
                mViewModel.sheetChoseList();
            });

        }

        //  60秒后触发
        handler.postDelayed(waitForMatch, 60 * 1000);

    }

    private void parseIntent() {
        int type = getIntent().getIntExtra(Sys.EXTRA, Sys.TYPE_PRIVATE_REFER);
        if (type == Sys.TYPE_DIAGNOSE_ONLINE) {
            mBinding.topBar.setCenterText(getString(R.string.online_consultation));
        } else {
            mBinding.topBar.setCenterText(getString(R.string.consult));
        }
        mViewModel.setType(type);

        String referId = getIntent().getStringExtra(EXTRA_REFER_ID);
        mViewModel.setReferId(referId);
    }

    private void observe() {
        mViewModel.choseLive.observe(this, choseBean -> {

            List<DoctorBean> doctor = choseBean.getDoctor();
            List<DoctorBean> com = choseBean.getCom();

            int doctorSize = doctor.size();
            for (int i = 0; i < 3 - doctorSize; i++) {
                DoctorBean doctorBean = new DoctorBean();
                doctorBean.setName("系统匹配中");
                doctor.add(doctorBean);
            }

            if (mViewModel.getType() == Sys.TYPE_PRIVATE_REFER) {
                int counselorSize = com.size();
                for (int i = 0; i < 3 - counselorSize; i++) {
                    DoctorBean doctorBean = new DoctorBean();
                    doctorBean.setName("系统匹配中");
                    com.add(doctorBean);
                }
            }

            adapter.getData().clear();

            if (doctor != null && !doctor.isEmpty()) {
                adapter.getData().add(new HomeRecommandBean("医生", doctor, 2));
            }

            if (mViewModel.getType() == Sys.TYPE_PRIVATE_REFER) {
                if (com != null && !com.isEmpty()) {
                    adapter.getData().add(new HomeRecommandBean("咨询师", com, 3));
                }
            }

            adapter.notifyDataSetChanged();


            int flag = choseBean.getFlag();
            if (flag == 1) {  //  有人接单

                // 开始倒计时
                mViewModel.startTimer();
                mBinding.tvHint.setVisibility(View.VISIBLE);
                mBinding.btnFinish.setText(R.string.cancel_cousult);
                mBinding.btnFinish.setVisibility(View.VISIBLE);

            } else {

                mViewModel.stopTimer();

                if (waitTimeout) {
                    mBinding.tvHint.setVisibility(View.GONE);
                    mBinding.btnFinish.setVisibility(View.VISIBLE);
                    mBinding.btnFinish.setText(R.string.no_one_recive);
                } else {

                    mBinding.tvHint.setVisibility(View.GONE);
                    mBinding.btnFinish.setVisibility(View.GONE);
                }

            }
        });

        mViewModel.timerLive.observe(this, millisUntilFinished -> {
            mBinding.tvHint.setText(getString(R.string.auto_cancel_60_second_after, (int) (millisUntilFinished / 1000)));
        });

        mViewModel.timerFinishLive.observe(this, aBoolean -> {
            finish();
        });

        mViewModel.chooseSheetUserLive.observe(this, doctorBean -> {

            mViewModel.setCancel(false);

            if (mViewModel.getType() == Sys.TYPE_PRIVATE_REFER) {
                //  进入聊天页面
                NimUtils.startP2PSession(this, doctorBean.getYunxin_accid());
                finish();

            } else if (mViewModel.getType() == Sys.TYPE_DIAGNOSE_ONLINE) {
                mViewModel.setSelectedAccid(doctorBean.getYunxin_accid());
                //  进入视频聊天界面
                MatchedActivityPermissionsDispatcher.startVideoCallWithPermissionCheck(this);
            }
        });
    }

    private boolean waitTimeout = false;
    private Handler handler = new Handler();
    private Runnable waitForMatch = () -> {
        waitTimeout = true;
        if (mViewModel.isCountDowning()) {
            return;
        }
        mBinding.btnFinish.setVisibility(View.VISIBLE);
        mBinding.btnFinish.setText(R.string.no_one_recive);
    };

    private void initRecyclerView() {
        adapter = new MatchedAdapter();
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        mViewModel.cancelSheet();
        handler.removeCallbacks(waitForMatch);
        super.onDestroy();
    }

    @NeedsPermission(value = {Manifest.permission.CAMERA})
    public void startVideoCall() {
        NimUtils.startVideoCall(this, mViewModel.getSelectedAccid());
        finish();
    }

    @OnPermissionDenied(value = {Manifest.permission.CAMERA})
    public void onCameraPermissionDemind() {
        showDeniedDialog("您拒绝了摄像头权限，使该功能无法使用，是否现在去开启？");
    }

    @OnNeverAskAgain(value = {Manifest.permission.CAMERA})
    void onCameraNeverAsk() {
        showDeniedDialog("您拒绝了摄像头权限，使该功能无法使用，是否现在去开启？");
    }

    private void showDeniedDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mViewModel.requestCallPermissionDoc.set(true);
                    AppUtils.launchAppDetailsSettings(getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel.requestCallPermissionDoc.get()) {
            mViewModel.requestCallPermissionDoc.set(false);
            MatchedActivityPermissionsDispatcher.startVideoCallWithPermissionCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MatchedActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private class MatchedAdapter extends BaseQuickAdapter<HomeRecommandBean<DoctorBean>, BaseViewHolder> {

        public MatchedAdapter() {
            super(R.layout.item_main_normal_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeRecommandBean<DoctorBean> item) {
            ItemMainNormalLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);

            layoutBinding.getRoot().setBackgroundResource(R.drawable.shadow_gray_bg);
            layoutBinding.tvChannelName.setBackgroundResource(R.drawable.left_red_gray_square_bg);
            layoutBinding.tvChannelName.setText(item.getName());

//            MainDoctorAdapter adapter = (MainDoctorAdapter) layoutBinding.recycler.getAdapter();
//            if (adapter == null) {
//                adapter = new MainDoctorAdapter(item.getType());
//                layoutBinding.recycler.swapAdapter(adapter, false);
//            }
//            adapter.setHandler(this);


            MatchedDoctorAdapter adapter = (MatchedDoctorAdapter) layoutBinding.recycler.getAdapter();
            if (adapter == null) {
                adapter = new MatchedDoctorAdapter();
                layoutBinding.recycler.swapAdapter(adapter, false);
            }

            adapter.replaceData(item.getGoodsLists());

        }

    }

    private class MatchedDoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder>
            implements ClickEventHandler<DoctorBean> {

        public MatchedDoctorAdapter() {
            super(R.layout.item_matched_doctor_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            ItemMatchedDoctorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setListener(this);
            layoutBinding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, DoctorBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            if (TextUtils.isEmpty(bean.getId())) {
                return;
            }
            mViewModel.chooseSheetUsers(bean);

        }
    }
}
