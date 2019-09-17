package com.chengzi.app.ui.homepage.user.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CustomTabFactory;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityUserHomePageBinding;
import com.chengzi.app.databinding.TabUserHomepageLayoutBinding;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.homepage.activity.FansOrAttentionActivity;
import com.chengzi.app.ui.homepage.bean.UserHeaderInfoBean;
import com.chengzi.app.ui.homepage.user.fragment.UserAnswerFragment;
import com.chengzi.app.ui.homepage.user.fragment.UserAskFragment;
import com.chengzi.app.ui.homepage.user.fragment.UserCollectFragment;
import com.chengzi.app.ui.homepage.user.fragment.UserPublishFragment;
import com.chengzi.app.ui.homepage.user.viewmodel.UserHomePageViewModel;
import com.chengzi.app.ui.mine.activity.editinfo.EditInfoUserActivity;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;
import com.chengzi.app.utils.NumberShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 用户主页
 *
 * @ClassName:UserHomePageActivity
 * @PackageName:com.yimei.app.ui.homepage.user.activity
 * @Create On 2019/4/19 0019   15:37
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class UserHomePageActivity extends BaseActivity<ActivityUserHomePageBinding,
        UserHomePageViewModel> implements View.OnClickListener {

    public static final String EXTRA_TARGET_USER_ID = "extra_target_user_id";

    public static void start(Context context, String targetId) {
        context.startActivity(new Intent(context, UserHomePageActivity.class)
                .putExtra(EXTRA_TARGET_USER_ID, targetId)
        );
    }

    public static void startSelf(Context context) {
        Intent intent = new Intent(context, UserHomePageActivity.class);
        intent.putExtra(EXTRA_TARGET_USER_ID, AccountHelper.getUserId());
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_home_page;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        parseIntent();

        mBinding.setListener(this);
        mBinding.setHideChatBtn(AccountHelper.getIdentity() > 1);

        mBinding.swipeRefreshView.setColorSchemeResources(R.color.colorPrimary);
        mBinding.swipeRefreshView.setOnRefreshListener(() -> {
            mViewModel.userInfo();
            mViewModel.refreshLive.postValue(true);
        });

        mBinding.viewPager.setOffscreenPageLimit(5);
        mBinding.viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.swipeRefreshView.setEnabled(verticalOffset >= 0);
            }
        });

        initTab();
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        observe();
        showLoading(Sys.LOADING);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.userInfo();
    }

    private void observe() {
        mViewModel.userInfoLiveData.observe(this, bean -> {
            dismissLoading();
            mBinding.swipeRefreshView.setRefreshing(false);
            if (bean != null) {
                mBinding.setBean(bean);
                mBinding.setInit(true);
                //自己的主页-->(隐藏聊天关注)显示编辑信息按钮
                boolean isSelf = TextUtils.equals(mViewModel.getTargetUserId(), AccountHelper.getUserId());
                mBinding.setIsSelef(isSelf);

                //性别背景
                mBinding.llSex.setBackgroundResource(TextUtils.equals(bean.getGender(), "1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);

//                String collection_num = ;

                mBinding.btnMyAttention.setText(createSpan(NumberShowUtils.processNumber(bean.getCollection_num()), R.string.attention));
                mBinding.btnMyFans.setText(createSpan(NumberShowUtils.processNumber(bean.getFans_num()), R.string.fans));
                mBinding.btnMyThumbup.setText(createSpan(NumberShowUtils.processNumber(bean.getLikes_num()), R.string.thumbup));

                mBinding.llNumContainer.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(bean.getCover())) {
                    ImageLoader.loadImage(mBinding.ivBackGround, bean.getCover(), 0, R.drawable.yonghuxinxi_beijing);
                    mBinding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
                } else {
                    mBinding.ivBackGround.setImageResource(R.drawable.yonghuxinxi_beijing);
                }

                mBinding.btnAttention.setText(bean.getIs_collection() == 1 ? "取消关注" : "关注");

                //发表 收藏 提问 回答
                userPublishBinding.textView.setText(getString(R.string.user_publish, NumberShowUtils.processNumber(bean.getPublish_count())));
                userCollectBinding.textView.setText(getString(R.string.user_collect, NumberShowUtils.processNumber(bean.getCollect_count())));
                userAskBinding.textView.setText(getString(R.string.user_ask, NumberShowUtils.processNumber(bean.getAsk_count())));
                userAnswerBinding.textView.setText(getString(R.string.user_answer, NumberShowUtils.processNumber(bean.getResponse_count())));
            }
        });

        //主页更换封面
        mViewModel.changeCoverLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                if (TextUtils.isEmpty(mViewModel.cover_image.get())) {
                    mBinding.ivBackGround.setImageResource(R.drawable.yonghuxinxi_beijing);
                } else {
                    ImageLoader.loadImage(mBinding.ivBackGround, mViewModel.cover_image.get(),
                            0, R.drawable.yonghuxinxi_beijing);
                    mBinding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
                }

            }
        });

        //关注/取消关注
        mViewModel.findFollowLiveData.observe(this, integer -> {
            dismissLoading();
            if (mBinding.getBean() == null) {
                return;
            }
            mBinding.getBean().setIs_collection(integer);
            mBinding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);
            EventBus.getDefault().post(new AttentionEvent(mViewModel.getTargetUserId(), integer == 1));

        });
    }

    private CharSequence createSpan(String num, int strRes) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));

        spannableStringBuilder.append(num, fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("\n");
        spannableStringBuilder.append(getString(strRes));
        return spannableStringBuilder;
    }

    private void parseIntent() {
        String targetUserId = getIntent().getStringExtra(EXTRA_TARGET_USER_ID);
        mViewModel.setTargetUserId(targetUserId);
    }

    @Subscribe
    public void onAttention(AttentionEvent event) {

        if (TextUtils.equals(mViewModel.getTargetUserId(), event.getUserId())) {
            int integer = event.isAttention() ? 1 : 0;
            mBinding.getBean().setIs_collection(integer);
            mBinding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);
            mViewModel.userInfo();
        }
    }

    private TabUserHomepageLayoutBinding userPublishBinding;
    private TabUserHomepageLayoutBinding userCollectBinding;
    private TabUserHomepageLayoutBinding userAskBinding;
    private TabUserHomepageLayoutBinding userAnswerBinding;

    private void initTab() {
        mBinding.tabLayout.setCustomTabFactory(new CustomTabFactory() {
            @Override
            public CustomTabLayout.Tab newTab(CharSequence text) {
                TabUserHomepageLayoutBinding tabBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.tab_user_homepage_layout,
                        mBinding.tabLayout, false);
                switch (text.toString()) {
                    case "0":
                        userPublishBinding = tabBinding;
                        userPublishBinding.textView.setText(getString(R.string.user_publish, "0"));
                        break;
                    case "1":
                        userCollectBinding = tabBinding;
                        userCollectBinding.textView.setText(getString(R.string.user_collect, "0"));
                        break;
                    case "2":
                        userAskBinding = tabBinding;
                        userAskBinding.textView.setText(getString(R.string.user_ask, "0"));
                        break;
                    case "3":
                        userAnswerBinding = tabBinding;
                        userAnswerBinding.textView.setText(getString(R.string.user_answer, "0"));
                        break;
                }
                CustomTabLayout.Tab tab = mBinding.tabLayout.newTab().setCustomView(tabBinding.getRoot());
                return tab;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (!ClickEvent.shouldClick(v)) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_back_ground:      //更换封面-->自己
                if (mViewModel.getTargetUserId().equals(AccountHelper.getUserId())) {
                    new SelectDialog(this)
                            .setTvText1("更换封面")
                            .setText1Listener(v1 -> {
//                                相册
                                CertifiedHelp.openPicture(this, 1);
                            })
                            .setTvCameraVisibility(false)
                            .show();
                }
                break;
            case R.id.btn_chat:      //聊天
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                UserHeaderInfoBean infoBean = mViewModel.userInfoLiveData.getValue();
                if (infoBean != null && !TextUtils.isEmpty(infoBean.getYunxin_accid())) {
                    String yunxin_accid = infoBean.getYunxin_accid();
                    NimUtils.startP2PSession(this, yunxin_accid);
                } else {
                    ToastUtils.showShort("对方的云信id不存在，让他重新登录");
                }
                break;
            case R.id.btn_attention:      //关注/取消关注
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                showLoading(Sys.LOADING);
                if (mBinding.getBean() == null) {
                    return;
                }
                int is_collection = mBinding.getBean().getIs_collection();
                mViewModel.findFollow(is_collection == 1);
                break;
            case R.id.btn_compile_profile://编辑信息
                goActivity(EditInfoUserActivity.class);
                break;
            case R.id.btn_my_attention:     // 关注
                FansOrAttentionActivity.start(this, Sys.EXTRA_ATTENTION, mViewModel.getTargetUserId());
                break;
            case R.id.btn_my_fans:          // 粉丝
                FansOrAttentionActivity.start(this, Sys.EXTRA_FANS, mViewModel.getTargetUserId());
                break;
            case R.id.btn_my_thumbup:       // 赞
                UserThumbUpActivity.start(this, mViewModel.getTargetUserId());
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Sys.REQUEST_CODE_PIC://更换封面
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                upload(picPath);
                break;
        }
    }

    private void upload(String images) {
        showLoading("封面更换中");
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String paths) {
                //更换封面
                mViewModel.cover_image.set(paths);
                mViewModel.changeCover();
            }

            @Override
            public void onError(AliError errors) {
                dismissLoading();
                Log.d("ALI_UP", "onError: " + errors.toString());
                toast("上传失败");
            }
        });
    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return UserPublishFragment.newInstance(mViewModel.getTargetUserId(), false);
                case 1:
                    return UserCollectFragment.newInstance(mViewModel.getTargetUserId());
                case 2:
                    return UserAskFragment.newInstance(mViewModel.getTargetUserId());
                case 3:
                    return UserAnswerFragment.newInstance(mViewModel.getTargetUserId());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }

}
