package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.BuildConfig;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemCaseListLayoutBinding;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;

public class CaseListAdapter extends BaseQuickAdapter<CaseBean, BaseViewHolder>
        implements ClickEventHandler<CaseBean> {

    private CaseBaseViewModel baseViewModel;
    private String categoryName;    //  案例类别，分享用

    //医院主页的案例不显示医院信息。
    private boolean is_show_head_layout = true;

    public void setBaseViewModel(CaseBaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CaseListAdapter() {
        super(R.layout.item_case_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, CaseBean item) {
        ItemCaseListLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setListener(this);
        binding.setBean(item);
        binding.setIsShowHead(is_show_head_layout);
        binding.setBaseViewModel(baseViewModel);
        binding.executePendingBindings();
    }

    public void setIsShowHeadLayout(boolean is_show_head_layout) {
        this.is_show_head_layout = is_show_head_layout;
    }

    @Override
    public void handleClick(View view, CaseBean bean) {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.root:
                CommentContainerActivity.start(view.getContext(), 2, bean.getId()
                        , CommentContainerActivity.COMMENT_CASE, bean.getPromotion_id());
                break;
            case R.id.btn_share:

                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }

                ShareDialog shareDialog = new ShareDialog();
                shareDialog.setTargetId(bean.getId());
                shareDialog.setType(ShareDialog.SHARE_TYPPE_CASE);
                shareDialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "ShareDialog");
                shareDialog.setTitle(categoryName);
                shareDialog.setTitle(bean.getName())
                        .setContent(bean.getContent())
                        .setUrl(String.format("%1$scase/%2$s",
                                TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl
                                , bean.getId()))
//                        .setUrl("http://api.inchengzi.com/case/" + bean.getId())
                        .setShareImage(bean.getLastImg());
                break;
            case R.id.btn_thumbup:
                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }
                baseViewModel.caseClickLike(bean);
                break;
        }
    }


}