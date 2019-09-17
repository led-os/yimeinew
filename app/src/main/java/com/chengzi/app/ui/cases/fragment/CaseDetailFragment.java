package com.chengzi.app.ui.cases.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.BuildConfig;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentCaseDetailBinding;
import com.chengzi.app.databinding.ItemBeautifulDiaryLayoutBinding;
import com.chengzi.app.databinding.ItemImage2LayoutBinding;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.ui.cases.viewmodel.CaseDetailViewModel;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.mine.activity.cases.AddCaseActivity;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.utils.NumberShowUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 案例详情
 *
 * @ClassName:CaseDetailFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/4/22 0022   13:19
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/22 0022 handongkeji All rights reserved.
 */
public class CaseDetailFragment extends BaseFragment<FragmentCaseDetailBinding, CaseDetailViewModel>
        implements View.OnClickListener, OnRefreshListener {

    private static final String EXTRA_PROMOTION_ID = "extra_promotion_id";

    private BeautifulDiaryAdapter adapter;

    public CaseDetailFragment() {
    }

    public static CaseDetailFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(CaseDetailFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new CaseDetailFragment();
            fragment.setArguments(args);
        }
        return (CaseDetailFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_case_detail;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        binding.setViewModel(viewModel);
        binding.setListener(this);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 30);
        adapter = new BeautifulDiaryAdapter(pool);
        binding.recyclerBeautifulCase.setAdapter(adapter);

        observe();
        viewModel.promotionCut();
    }

    private void parseIntent() {
        Intent intent = getActivity().getIntent();

        String targetId = intent.getStringExtra(Sys.EXTRA_TARGET_ID);
        viewModel.setCaseId(targetId);

        String promotionId = intent.getStringExtra(EXTRA_PROMOTION_ID);
        viewModel.setPromotionId(promotionId);

        int form = intent.getIntExtra("form", -1);

        if (form == 1) {
            ((CommentContainerActivity) getActivity()).setUpTopBar(R.drawable.anli_tianjia,
                    () -> AddCaseActivity.start(getActivity(), 2, viewModel.getCaseId()));
        } else if (form == 2) {
            ((CommentContainerActivity) getActivity()).setUpTopBar(R.drawable.nav_fenxiang,
                    () -> share());
        } else {
//            binding.btnThumbup.setEnabled(false);
        }
    }

    private void share() {

        if (AccountHelper.shouldLogin(getActivity())) {
            return;
        }

        if (adapter.getData().isEmpty()) {
            ToastUtils.showShort("该案例错误");
            return;
        }
        CaseNoteListBean item = adapter.getData().get(0);

        ShareDialog shareDialog = new ShareDialog();
        shareDialog.setTargetId(item.getCases_id());
        shareDialog.setType(ShareDialog.SHARE_TYPPE_CASE);
        shareDialog.show(getChildFragmentManager(), "ShareDialog");
        shareDialog.setTitle(item.getCasename())
                .setContent(item.getContent())
                .setUrl(String.format("%1$scase/%2$s",
                        TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl
                        , item.getCases_id()));
//                .setUrl("http://api.inchengzi.com/case/" + item.getCases_id());
        List<String> images = item.getImages();
        if (images.isEmpty()) {
            shareDialog.setShareImage(null);
        } else {
            shareDialog.setShareImage(images.get(0));
        }
    }

    private void observe() {
        viewModel.caseNoteListLive.observe(this, caseNoteList -> {
            adapter.setNewData(caseNoteList);
            binding.getRoot().setVisibility(View.VISIBLE);

            if (caseNoteList == null || caseNoteList.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteList.get(0);
            CaseNoteListBean.UserEntity user = bean.getUser();
            if (user != null) {
                binding.setAvatar(user.getHeadimg());
                binding.setHospitalName(user.getName());
            }
            binding.setCategoryName(bean.getCasename());

            viewModel.likeNumber.set(NumberShowUtils.processNumber(bean.getLike_number()));
            viewModel.like.set(bean.isLike());

            ((CommentContainerActivity) getActivity()).setUpCommentNumber(bean.getComment_number());

        });

        viewModel.caseThumbUpLive.observe(this, integer -> {
            ToastUtils.showShort(integer == 1 ? "点赞成功" : "已取消点赞");
            viewModel.like.set(integer == 1);
            viewModel.likeNumber.set(String.valueOf(Integer.valueOf(viewModel.likeNumber.get()) + (integer == 1 ? 1 : -1)));
            viewModel.thumbUpObservable.update();
            EventBus.getDefault().post(new CaseThumbUpEvent(viewModel.getCaseId(), integer == 1));
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.caseNoteList();
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        if (AccountHelper.shouldLogin(getActivity())) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_thumbup:  //  案例点赞
                viewModel.caseClickLike();
                break;
            case R.id.ll_hosipital:
                List<CaseNoteListBean> listBeans = viewModel.caseNoteListLive.getValue();
                if (listBeans == null || listBeans.isEmpty()) {
                    return;
                }
                CaseNoteListBean.UserEntity user = listBeans.get(0).getUser();
                if (user == null || TextUtils.isEmpty(user.getId())) {
                    return;
                }
                HospitalHomePageActivity.start(getActivity(), user.getId());
                break;
        }
    }

    @Override
    public void onRefresh() {
        viewModel.caseNoteList();
    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((RefreshListenerRegistry) getActivity()).register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((RefreshListenerRegistry) getActivity()).unRegister(this);
    }

    private static class BeautifulDiaryAdapter extends BaseQuickAdapter<CaseNoteListBean, BaseViewHolder> {

        private RecyclerView.RecycledViewPool pool;

        public BeautifulDiaryAdapter(RecyclerView.RecycledViewPool pool) {
            super(R.layout.item_beautiful_diary_layout);
            this.pool = pool;
        }

        @Override
        protected void convert(BaseViewHolder helper, CaseNoteListBean item) {

            ItemBeautifulDiaryLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);

            ImageAdapter imageAdapter = new ImageAdapter();
            binding.nineGridView.swapAdapter(imageAdapter, false);
            binding.nineGridView.setRecycledViewPool(pool);
            imageAdapter.setNewData(item.getImages());
            imageAdapter.setOnItemClickListener((adapter1, view, position) -> {
                PictureSwitcherActivity.start(view.getContext(), (ArrayList<String>) imageAdapter.getData(), position);
            });
        }
    }

    private static class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ImageAdapter() {
            super(R.layout.item_image2_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ItemImage2LayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setUrl(item);
            layoutBinding.executePendingBindings();
        }
    }
}
