package com.chengzi.app.ui.mine.activity.onlinecases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.adapter.NineImageAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityOnlineCasesBindingImpl;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.common.activity.VideoPlayActivity;
import com.chengzi.app.ui.mine.bean.CaseInfoBean;
import com.chengzi.app.ui.mine.viewmodel.OnlineCasesViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线病例暂无数据/有数据 -->普通用户
 *
 * @ClassName:OnlineCasesActivity
 * @PackageName:com.yimei.app.ui.mine.activity.onlinecases
 * @Create On 2019/4/3 0003   13:40
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class OnlineCasesActivity extends BaseActivity<ActivityOnlineCasesBindingImpl, OnlineCasesViewModel> {

    public static final String TARGET_USER_ID = "target_user_id";
    public static final String ACTION_FOR_RESULT = "action_for_result";
    public static final String ACTION_FOR_VIEW = "action_for_view";

    //图片 展示图片的adapter
//    private ArrayList<String> images;
    private NineImageAdapter imageAdapter;
    //    private CaseInfoBean data;
    private boolean isFirstIn = true;

    public static void start(Context context, String targetUserId) {
        Intent intent = new Intent(context, OnlineCasesActivity.class);
        intent.putExtra(TARGET_USER_ID, targetUserId);
        context.startActivity(intent);
    }

    public static void startForResult(Context context, int requestCode) {
        Intent intent = new Intent(context, OnlineCasesActivity.class);
        intent.putExtra(TARGET_USER_ID, AccountHelper.getUserId());
        intent.putExtra(ACTION_FOR_RESULT, true);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void startForView(Context context,  String targetUserId) {
        Intent intent = new Intent(context, OnlineCasesActivity.class);
        intent.putExtra(TARGET_USER_ID, targetUserId);
        intent.putExtra(ACTION_FOR_VIEW, true);
        ((Activity) context).startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_online_cases;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstIn) {
            isFirstIn = false;
            showLoading(Sys.LOADING);
        }
        mViewModel.caseInfo();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setListener(clickListener);

        initRecyclerView();

        //添加病例
        mBinding.tvAddCases.setOnClickListener(v -> {
            goActivity(AddOnlineCasesActivity.class);
        });
        //编辑
        mBinding.topBar.setOnRightClickListener(v -> {
            CaseInfoBean bean = mViewModel.getCaseInfoBean();
            if (bean != null) {
                if (mViewModel.isForwardCase()) {
                    //  发送病例给咨询师/医生
                    Intent intent = new Intent();
                    intent.putExtra("case_uid", bean.getUser_id());
                    intent.putExtra("avatar",bean.getUser_image());
                    intent.putExtra("nickname",bean.getUser_name());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    AddOnlineCasesActivity.start(this, bean);
                }
            }
        });

        //获取病例信息
        mViewModel.caseInfoLiveData.observe(this, caseInfoBean -> {
            dismissLoading();
            //获取数据正确 显示病例
            if (caseInfoBean != null) {
                mBinding.llNoData.setVisibility(View.GONE);
                mBinding.nsvHaveData.setVisibility(View.VISIBLE);
                if (mViewModel.isForwardCase()) {
                    mBinding.topBar.setRightText("转发");
                }else if (mViewModel.isForView()){
                    mBinding.topBar.setRightText("");
                }else{
                    mBinding.topBar.setRightText("编辑");
                }
                mBinding.setBean(caseInfoBean);
                mBinding.setUrl(caseInfoBean.getUser_image());
                //补充信息->图片  / 视频
                setImageOrVideo(caseInfoBean);
                mViewModel.setCaseInfoBean(caseInfoBean);
            } else {    //获取数据失败 显示去绑定数据
                mBinding.llNoData.setVisibility(View.VISIBLE);
                mBinding.nsvHaveData.setVisibility(View.GONE);
                mBinding.topBar.setRightText("");
            }
        });
    }

    private void parseIntent() {
        String targetUserId = getIntent().getStringExtra(TARGET_USER_ID);
        mViewModel.setTargetUserId(targetUserId);
        boolean forwardCase = getIntent().getBooleanExtra(ACTION_FOR_RESULT, false);
        mViewModel.setForwardCase(forwardCase);
        boolean onlyView = getIntent().getBooleanExtra(ACTION_FOR_VIEW, false);
        mViewModel.setForView(onlyView);
    }

    private void setImageOrVideo(CaseInfoBean data) {
        //    图片视频类型 0-图片视频都没有，1-图片 2-视频
        String image_type = data.getImage_type();
        if (TextUtils.equals(image_type, "1")) {
            mBinding.rlVideo.setVisibility(View.GONE);
            mBinding.mRecyclerView.setVisibility(View.VISIBLE);
            List<String> images = data.getImage();
            imageAdapter.setNewData(images);
        } else if (TextUtils.equals(image_type, "2")) {
            mBinding.rlVideo.setVisibility(View.VISIBLE);
            mBinding.mRecyclerView.setVisibility(View.GONE);
            ImageLoader.loadImage(mBinding.videoImage, data.getCover_image(), 0, R.color.colorGray);
        }
    }

    private void initRecyclerView() {
        mBinding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapter = new NineImageAdapter();
        mBinding.mRecyclerView.setAdapter(imageAdapter);
        //看大图
        imageAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position) -> {
            List<String> images = imageAdapter.getData();
            PictureSwitcherActivity.start(this, (ArrayList<String>) images, position);
        });
    }

    private ClickEventHandler<CaseInfoBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.btn_play:
            case R.id.video_image:
                if (TextUtils.equals(bean.getImage_type(), "2") && bean.getImage() != null && !bean.getImage().isEmpty()) {
                    VideoPlayActivity.start(this, bean.getImage().get(0), "");
                }
                break;
        }
    };
}