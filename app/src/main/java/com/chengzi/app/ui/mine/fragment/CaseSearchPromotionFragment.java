package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.BuildConfig;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentCaseSearchPromotionBindingImpl;
import com.chengzi.app.databinding.ItemCaseSearchPromoteLayoutBinding;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.mine.activity.popularize.IWantPopularizeActivity;
import com.chengzi.app.ui.mine.bean.CaseManageListBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.viewmodel.CaseSearchPromotionViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 个人中心 医院
 * ① 案例管理
 * ② 我要推广-> 案例站内搜索推广
 *
 * @ClassName:CaseSearchPromotionFragment
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/15
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 handongkeji All rights reserved.
 */
public class CaseSearchPromotionFragment extends LazyloadFragment<FragmentCaseSearchPromotionBindingImpl, CaseSearchPromotionViewModel> implements OrderStatusHelp.BeautyOrderTypeChange {

    //分类id
    private String cate_id;
    //1案例管理 / 案例站内搜索推广
    private int form;
    private PagingLoadHelper helper;
    private CaseSearchPromotionAdapter adapter;

    //设置的限额
    private String money;
    //设置的限额
//    private TextView tvPromoteMoney;
    //是否推广
    private ImageView ivPromote;
    private boolean is_extension;
    private CaseManageListBean caseManageListBean;
    private int postion = 0;

    public static CaseSearchPromotionFragment newInstance(String cate_id, int form) {
        CaseSearchPromotionFragment fragment = new CaseSearchPromotionFragment();
        Bundle args = new Bundle();
        args.putString("cate_id", cate_id);
        args.putInt("form", form);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_case_search_promotion;
    }

    @Override
    public void typeChange(int form) {
        if (this.form == form) {
            helper.onPulldown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        SuperObservableManager.unregisterObserver(OrderStatusHelp.BeautyOrderTypeChange.class, this);
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.BeautyOrderTypeChange.class, this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            cate_id = arguments.getString("cate_id");
            form = arguments.getInt("form", -1);
        }

        adapter = new CaseSearchPromotionAdapter();
        //解决闪烁问题
        adapter.setHasStableIds(true);
        binding.swipeRefreshView.setAdapter(adapter);
        ((SimpleItemAnimator) binding.swipeRefreshView.getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        //案例管理详情 推广案例详情
        viewModel.form = form;
        viewModel.cate_id = cate_id;
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            CaseManageListBean bean = adapter.getData().get(position);
            String cases_id = "";
            if (form == 1) {    //案例管理
                CaseManageListBean.HospitalInfoBean hospital_info = bean.getHospital_info();
                if (hospital_info != null) {
                    cases_id = bean.getCases_id();
                }
            } else {    //推广案例详情(自己医院的)
                cases_id = bean.getId();
            }
            CommentContainerActivity.start(getContext(), form, cases_id, CommentContainerActivity.COMMENT_CASE);
//            CaseDetailsActivity.start(getContext(), form, cases_id, bean.getCate_id(), hospital_img, hospital_name, catename, like_number, like);
//            toast("案例详情");
        });
        //设置限额  推广
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            caseManageListBean = adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_promote:   // 推广
                    ivPromote = (ImageView) adapter1.getViewByPosition(binding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote);
                    is_extension = caseManageListBean.isIs_extension();
                    showLoading(Sys.LOADING);
                    if (!is_extension) {  //开启
                        viewModel.addSearchPromotionItem(caseManageListBean.getId(), "5");
                    } else {
                        viewModel.removeSearchPromotionItem(caseManageListBean.getId(), "5");
                    }
                    break;
                case R.id.tv_set:       // 设置
                    this.postion = position;
                    new SetLimitDialog(getContext(), 2)
                            .setSureListener(text -> {
//                                tvPromoteMoney = (TextView) adapter1.getViewByPosition(binding.swipeRefreshView.getRecyclerView(), position, R.id.tv_promote_money);
                                money = text; ////推广限额:如果不为空时 判断是不是小于推广金额
                                if (!TextUtils.isEmpty(money)) {
                                    double v = Double.parseDouble(money);
                                    if (v < IWantPopularizeActivity.promotion_price) {
                                        toast(getResources().getString(R.string.want_popularize));
                                        return;
                                    }
                                }
                                showLoading(Sys.LOADING);  //5案例
                                viewModel.setSearchPromotionAmount(caseManageListBean.getId(), "5", text);
                            })
                            .show();
                    break;
                case R.id.tv_praise:       // 点赞
                    this.postion = position;
//                    if (!caseManageListBean.isLike()) { // 当前用户是否
                    String cases_id = "";
                    if (form == 1) {    //案例管理
                        CaseManageListBean.HospitalInfoBean hospital_info = caseManageListBean.getHospital_info();
                        if (hospital_info != null) {
                            cases_id = caseManageListBean.getCases_id();
                        }
                    } else {    //推广案例详情(自己医院的)
                        cases_id = caseManageListBean.getId();
                    }
                    CaseBean caseBean = new CaseBean();
                    caseBean.setLike_number(caseManageListBean.getLike_number());
                    caseBean.setLike(caseManageListBean.isLike());
                    caseBean.setId(cases_id);
                    viewModel.caseClickLike1(caseBean);
//                    }
                    break;
                case R.id.tv_share_num: //分享案例
                    share();
                    break;
            }
        });
        observe();
    }

    private void share() {
        if (AccountHelper.shouldLogin(getActivity())) {
            return;
        }
        if (adapter.getData().isEmpty()) {
            ToastUtils.showShort("该案例错误");
            return;
        }
        String cases_id = "";
        if (form == 1) {    //案例管理
            cases_id = caseManageListBean.getCases_id();
        } else {    //推广案例详情(自己医院的)
            cases_id = caseManageListBean.getId();
        }
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.setTargetId(cases_id);
        shareDialog.setType(ShareDialog.SHARE_TYPPE_CASE);
        shareDialog.show(getChildFragmentManager(), "ShareDialog");
        shareDialog.setTitle(caseManageListBean.getCatename())
                .setContent(caseManageListBean.getContent())
                .setUrl(String.format("%1$scase/%2$s",
                        TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl
                        , cases_id));
//                .setUrl("http://api.inchengzi.com/case/" + cases_id);
        String images = caseManageListBean.getAfter_img();
        if (images.isEmpty()) {
            shareDialog.setShareImage(null);
        } else {
            shareDialog.setShareImage(images);
        }
    }

    private void observe() {
        /*案例管理列表的数据*/
        viewModel.caseManageLive.observe(this, caseManageListBeans -> {
            if (caseManageListBeans != null && caseManageListBeans.size() > 0) {
                /*构造一个Bean类 用来 包含所有的数据  包含推广列表的数据  包含*/
                helper.onComplete(caseManageListBeans);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        /*点赞 请求的数据*/
        viewModel.caseThumbUpLiveData.observe(this, bean -> {
            dismissLoading();
            if (bean != null && bean.getData() != null) {
                //1-点赞成功 2-取消点赞
                String data = bean.getData();
                CaseManageListBean bean1 = adapter.getData().get(postion);
                if (data.equals("1")) {
                    bean1.setLike(true);
                    bean1.setLike_number(bean1.getLike_number() + 1);
                } else {
                    bean1.setLike(false);
                    if (bean1.getLike_number() > 0) {
                        bean1.setLike_number(bean1.getLike_number() - 1);
                    }
                }
                adapter.notifyItemChanged(postion);
            }
        });
        //设置限额
        viewModel.setSearchPromotionAmountLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                tvPromoteMoney.setText("¥" + money);
                adapter.getData().get(postion).setPreset_amount(money);
                adapter.notifyItemChanged(postion);
            }
        });
        //开启推广
        viewModel.addSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(true);
                caseManageListBean.setIs_extension(true);
            }
        });
        //关闭推广
        viewModel.removeSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                ivPromote.setSelected(false);
                caseManageListBean.setIs_extension(false);
            }
        });

        viewModel.caseDetailLive.observe(this,caseNoteListBeans -> {
            if (caseNoteListBeans == null || caseNoteListBeans.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteListBeans.get(0);
            for (CaseManageListBean caseBean : adapter.getData()) {
                if (TextUtils.equals(bean.getCases_id(),caseBean.getCases_id())) {
                    caseBean.setComment_number(bean.getComment_number()+"");
                    viewModel.commentObservable.update();
                    break;
                }
            }
        });
    }


    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 1) {
            return;
        }

        CaseManageListBean bean = null;
        for (CaseManageListBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getCases_id(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        CaseManageListBean bean = null;
        for (CaseManageListBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getCases_id(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }


    public class CaseSearchPromotionAdapter extends BaseQuickAdapter<CaseManageListBean, BaseViewHolder> {
        public CaseSearchPromotionAdapter() {
            super(R.layout.item_case_search_promote_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CaseManageListBean item) {
            ItemCaseSearchPromoteLayoutBinding bindings = DataBindingUtil.bind(helper.itemView);
            bindings.setBaseViewModel(viewModel);
            bindings.setBean(item);
            bindings.setUrl(item.getAfter_img());     // 术后照
            bindings.setUrlBefore(item.getPre_img()); // 术前照
            bindings.executePendingBindings();
            //1案例管理 / 案例站内搜索推广
            bindings.llCaseManage.setVisibility(form == 1 ? View.VISIBLE : View.GONE);
            bindings.tvCase.setVisibility(form == 1 ? View.VISIBLE : View.GONE);
            bindings.llPromote.setVisibility(form == 1 ? View.GONE : View.VISIBLE);
            bindings.tvCasePromotion.setVisibility(form == 1 ? View.GONE : View.VISIBLE);
            bindings.view.setVisibility(form == 1 ? View.GONE : View.VISIBLE);
            //是否推广
            boolean is_extension = item.isIs_extension();
            bindings.tvPromote.setSelected(is_extension);

            //是否可以点赞 true-可点赞 false-不可点赞
            boolean like = item.isLike();
            Drawable drawable_n = null;
            if (form == 1) {    //案例管理可以点赞
                drawable_n = getResources().getDrawable(like ? R.drawable.dianzan_yidian : R.drawable.dianzan_weidian);
            } else {            //推广案例不能点赞
                drawable_n = getResources().getDrawable(R.drawable.dianzan_weidian);
            }
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            bindings.tvPraise.setCompoundDrawables(drawable_n, null, null, null);
            helper.addOnClickListener(R.id.tv_set)
                    .addOnClickListener(R.id.tv_promote)
                    .addOnClickListener(R.id.tv_praise)
                    .addOnClickListener(R.id.tv_share_num)
            ;
        }
    }

}
