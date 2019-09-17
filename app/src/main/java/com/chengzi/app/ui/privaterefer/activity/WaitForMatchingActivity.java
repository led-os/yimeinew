package com.chengzi.app.ui.privaterefer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityWaitForMatchingBinding;
import com.chengzi.app.ui.privaterefer.ReferObservable;
import com.chengzi.app.ui.privaterefer.viewmodel.WaitForMatchViewModel;

/**
 * 等待匹配
 *
 * @ClassName:WaitForMatchingActivity
 * @PackageName:com.yimei.app.ui.privaterefer.activity
 * @Create On 2019/4/9 0009   10:54
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
public class WaitForMatchingActivity extends BaseActivity<ActivityWaitForMatchingBinding,
        WaitForMatchViewModel> implements View.OnClickListener {

    private static final String EXTRA_REFER_ID = "extra_refer_id";
    private boolean referSuccess;   //  咨询成功 ，已经选择了咨询对象

    /**
     * @param context
     * @param type    0 私享咨询   1  在线诊断
     * @param referId 私享咨询 或 在线诊断的 id
     */
    public static void start(Context context, int type, String referId) {
        Intent intent = new Intent(context, WaitForMatchingActivity.class);
        intent.putExtra(Sys.EXTRA, type);
        intent.putExtra(EXTRA_REFER_ID, referId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_wait_for_matching;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);

        parseIntent();
        observe();

        if (savedInstanceState == null) {

            //  每 5秒发一次请求，查询是否有医生/咨询师接单
            ReferObservable.interval(this, () -> {
                mViewModel.sheetChoseList();
            });

            mViewModel.startTimer();

        }
    }

    private void parseIntent() {
        int type = getIntent().getIntExtra(Sys.EXTRA, Sys.TYPE_PRIVATE_REFER);
        if (type == Sys.TYPE_DIAGNOSE_ONLINE) {
            mBinding.topBar.setCenterText(getString(R.string.online_consultation));
        } else {
            mBinding.topBar.setCenterText(getString(R.string.consult));
            mBinding.layoutCounselor.setVisibility(View.VISIBLE);
        }
        mViewModel.setType(type);
        String referId = getIntent().getStringExtra(EXTRA_REFER_ID);
        mViewModel.setReferId(referId);

    }

    private void observe() {
        mViewModel.choseLive.observe(this, referChoseBean -> {
            int flag = referChoseBean.getFlag();
            if (flag == 1) {  //  有人接单
                referSuccess = true;
                MatchedActivity.start(this, mViewModel.getType(), mViewModel.getReferId());
                onBackPressed();
            }
        });

        mViewModel.timerFinishLive.observe(this, aBoolean -> {
            mBinding.btnFinish.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:   //  取消咨询
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (!referSuccess) {
            mViewModel.cancelSheet();
        }
        super.onDestroy();
    }
}
