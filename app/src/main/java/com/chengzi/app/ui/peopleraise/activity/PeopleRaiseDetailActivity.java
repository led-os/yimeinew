package com.chengzi.app.ui.peopleraise.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.handongkeji.widget.KeyboardLayout1;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PeopleRaiseAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPeopleRaiseDetailBinding;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.fragment.RaiseCommentFragment;
import com.chengzi.app.ui.peopleraise.fragment.RaiseDetailHeaderFragment;
import com.chengzi.app.ui.peopleraise.viewmodel.PeopleRaiseDetailViewModel;

/**
 * 美人筹详情
 *
 * @ClassName:PeopleRaiseDetailActivity
 * @PackageName:com.yimei.app.ui.peopleraise.activity
 * @Create On 2019/4/9 0009   16:20
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */

public class PeopleRaiseDetailActivity extends BaseActivity<ActivityPeopleRaiseDetailBinding,
        PeopleRaiseDetailViewModel> implements ClickEventHandler {

    public static final String EXTRA_RAISE_ID = "extra_raise_id";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    private PeopleRaiseAdapter raiseAdapter;

    public static void start(Context context, String id, String categoryId) {
        Intent intent = new Intent(context, PeopleRaiseDetailActivity.class);
        intent.putExtra(EXTRA_RAISE_ID, id);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        context.startActivity(intent);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_people_raise_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //
        mViewModel.setTextView(mBinding.btnParticipate,true);

        parseIntent();

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
        initRecommandRecycler();
        setKeyboardListener();
        observe();

        mViewModel.planRecommendList(mViewModel.getPlanId());
        mViewModel.userInfo();

    }

    private void parseIntent() {
        String planId = getIntent().getStringExtra(EXTRA_RAISE_ID);
        int type = getIntent().getIntExtra(Sys.EXTRA, 1);
        String categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        mViewModel.setPlanId(planId);
//        mViewModel.setType(type);
        mViewModel.setCategoryId(categoryId);
    }

    private void observe() {
        mViewModel.recommendRaiseLive.observe(this, raiseBeans -> {

            raiseAdapter.setNewData(raiseBeans);
            mBinding.viewSwitcher.getChildAt(1).setVisibility(View.VISIBLE);
            mBinding.viewSwitcher.getChildAt(0).setVisibility(View.GONE);

        });

        mViewModel.addCommentLive.observe(this, aBoolean -> {
            ToastUtils.showShort("评论成功");
        });

        mViewModel.joinPlanLive.observe(this, aBoolean -> {
            dismissLoading();
            int identity = AccountHelper.getIdentity();
            if (identity == 1) {
                ConfirmRaiseOrderActivity.start(this, mViewModel.getPlanId());
            } else {
                ToastUtils.showShort("参与成功");
            }
            finish();
        });

        mViewModel.userInfoLiveData.observe(this, userInfoBean -> {
            if (userInfoBean == null) {
                return;
            }
            String auth_status = userInfoBean.getAuth_status();
            mViewModel.setAuthStatus(Integer.valueOf(auth_status));
        });
    }

    private void initRecommandRecycler() {

        raiseAdapter = new PeopleRaiseAdapter();
        raiseAdapter.setHeaderAndEmpty(true);
        raiseAdapter.setDetailRecommend(true);
        raiseAdapter.bindToRecyclerView(mBinding.recyclerRecommand);
        raiseAdapter.setEmptyView(R.layout.empty_layout);

        raiseAdapter.addHeaderView(initHeader());
        raiseAdapter.addHeaderView(initComment());

        raiseAdapter.setOnItemClickListener((adapter, view, position) -> {
            PeopleRaiseDetailActivity.start(this,
                    mViewModel.getPlanId(), mViewModel.getCategoryId()
            );
        });

    }

    private View initHeader() {
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setId(R.id.raise_detail_header_container0);
        frameLayout.post(() -> {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.raise_detail_header_container0, RaiseDetailHeaderFragment
                                    .newInstance(getSupportFragmentManager()),
                            RaiseDetailHeaderFragment.class.getSimpleName())
                    .commit();
        });
        return frameLayout;
    }

    private View initComment() {
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setId(R.id.raise_detail_header_container1);
        frameLayout.post(() -> {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.raise_detail_header_container1, RaiseCommentFragment
                                    .newInstance(getSupportFragmentManager())
                            , RaiseCommentFragment.class.getSimpleName())
                    .commit();
        });
        return frameLayout;
    }

    @Override
    public void handleClick(View view, Object bean) {

        if (!ClickEvent.shouldClick(view)) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_comment:  //  我要评论
                mViewModel.isComment.set(true);
                mBinding.editComment.setFocusable(true);
                mBinding.editComment.setFocusableInTouchMode(true);
                mBinding.editComment.requestFocus();
                KeyboardUtils.showSoftInput(this);
                break;
            case R.id.btn_participate:   //  我要参与
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                int identity = AccountHelper.getIdentity();
                if (identity == 2) {
                    ToastUtils.showShort("医生不能参与美人筹");
                    return;
                }
                if (identity == 3) {
                    ToastUtils.showShort("咨询师不能参与美人筹");
                    return;
                }

                RaiseBean raiseBean = mViewModel.raiseLive.getValue();
                if (raiseBean != null) {
                    int status = raiseBean.getStatus();
                    if (status == 2) {
                        ToastUtils.showShort("订单已达成");
                        return;
                    }
                    if (status == 3) {
                        ToastUtils.showShort("订单已完成");
                        return;
                    }
                    if (status == 4) {
                        ToastUtils.showShort("订单已取消");
                        return;
                    }
                }

                if (AccountHelper.getIdentity() > 1) {
                    int authStatus = mViewModel.getAuthStatus();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                showLoading("");
                mViewModel.joinPlan();
                break;
            case R.id.btn_send:
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.content.get())) {
                    ToastUtils.showShort("评论内容不能为空");
                    return;
                }
                mViewModel.planCommentAdd();
                KeyboardUtils.hideSoftInput(this);
                break;
        }
    }

    private Rect rect = new Rect();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            rect.setEmpty();
            mBinding.llComment.getGlobalVisibleRect(rect);
            if (mViewModel.isComment.get() && !rect.contains((int) rawX, (int) rawY)) {
                KeyboardUtils.hideSoftInput(this);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setKeyboardListener() {
        mBinding.keyboardLayout.setOnkbdStateListener(state -> {
            switch (state) {
                case KeyboardLayout1.KEYBOARD_STATE_INIT:
                    break;
                case KeyboardLayout1.KEYBOARD_STATE_HIDE:
                    mViewModel.isComment.set(false);
                    break;
                case KeyboardLayout1.KEYBOARD_STATE_SHOW:
                    break;
            }
        });
    }

}
