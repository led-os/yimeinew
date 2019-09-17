package com.chengzi.app.ui.search.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CaseListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSearchCaseBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.search.activity.AllGoodsActivity;
import com.chengzi.app.ui.search.viewmodel.SearchCaseViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果--案例
 *
 * @ClassName:SearchCaseFragment
 * @PackageName:com.yimei.app.ui.search.fragment
 * @Create On 2019/4/8 0008   16:23
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class SearchCaseFragment extends LazyloadFragment<FragmentSearchCaseBinding, SearchCaseViewModel>
        implements View.OnClickListener, SearchListener {

    public static final int REQUEST_CODE_SELECT_GOODS = 0x03;//选择案例类型

    private boolean needRefresh;
    private boolean lazyloaded;
    private PagingLoadHelper helper;
    private CaseListAdapter adapter;

    public SearchCaseFragment() {
    }

    public static SearchCaseFragment newInstance(String keyword) {
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, keyword);
        SearchCaseFragment fragment = new SearchCaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_search_case;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        String keyword = getArguments().getString(Sys.EXTRA);
        viewModel.setKeyword(keyword);

        binding.setListener(this);
        binding.setSelectedTabPos(viewModel.selectedTabPos);

        viewModel.setLifecycleOwner(this);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        binding.swipeRefreshView.setPulldownEnable(false);
        binding.swipeRefreshView.setEmptyView(null);
        adapter = new CaseListAdapter();
        adapter.setBaseViewModel(viewModel);
        binding.swipeRefreshView.setAdapter(adapter);

        helper.setEmptyListener(empty -> binding.viewSwitcher.setDisplayedChild(empty ? 2 : 1));
        helper.setStartListener(() -> binding.viewSwitcher.setDisplayedChild(0));

       observe();
    }

    private void observe(){

        viewModel.caseLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        viewModel.caseDetailLive.observe(this,caseNoteListBeans -> {
            if (caseNoteListBeans == null || caseNoteListBeans.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteListBeans.get(0);
            for (CaseBean caseBean : adapter.getData()) {
                if (TextUtils.equals(bean.getCases_id(),caseBean.getId())) {
                    caseBean.setComment_number(bean.getComment_number()+"");
                    viewModel.commentObservable.update();
                    break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all_goods: //  所有商品
                viewModel.selectedTabPos.set(0);
                startActivityForResult(new Intent(getActivity(), AllGoodsActivity.class), REQUEST_CODE_SELECT_GOODS,
                        data -> {
                            Bundle bundle = data.getExtras();
                            String first_id = bundle.getString("first_id");
                            String second_id = bundle.getString("second_id");
                            String third_id = bundle.getString("third_id");
                            viewModel.setCategoryId(first_id);
                            viewModel.setCategoryId2(second_id);
                            viewModel.setCategoryId3(third_id);

                            String categoryName = bundle.getString("categoryName");
                            int level = bundle.getInt("level", 1);
                            if (level == 1) {
                                binding.tvGoodCategory.setText(R.string.all_goods_type);
                            } else {
                                binding.tvGoodCategory.setText(categoryName);
                            }
                            helper.start();
                        });
                break;
            case R.id.layout_sort:   //  排序
                viewModel.selectedTabPos.set(1);
                DropDownPopup popup = new DropDownPopup(getActivity());
                popup.showAsDropDown(v);
                List<DropDownPopup.PopupBean> data = new ArrayList<>();
                data.add(new DropDownPopup.Item(0, "智能排序", "a", null));
                data.add(new DropDownPopup.Item(1, "最新回复", "commentNew", "1"));
                data.add(new DropDownPopup.Item(2, "最新创建", "createNew", "1"));
                data.add(new DropDownPopup.Item(3, "好评量", "goodsCom", "1"));
                popup.setData(data);
                popup.setOnDismissListener(() -> {
                    viewModel.selectedTabPos.set(-1);
                });
                popup.setSureListener(bean -> {
                    viewModel.setSortParam(bean);
                    binding.tvSortRule.setText(bean.getName());
                    helper.start();
                });
                break;
            case R.id.layout_origanization:  //  机构类型
                viewModel.selectedTabPos.set(2);
                popup = new DropDownPopup(getActivity());
                popup.showAsDropDown(v);
                data = new ArrayList<>();
                data.add(new DropDownPopup.Item(0, "不限类型", "hosipital_type", null));
                data.add(new DropDownPopup.Item(1, "民营机构", "hosipital_type", "1"));
                data.add(new DropDownPopup.Item(2, "公司机构", "hosipital_type", "2"));
                data.add(new DropDownPopup.Item(3, "品牌连锁", "hosipital_type", "3"));
                data.add(new DropDownPopup.Item(4, "生活美容机构", "hosipital_type", "4"));
                popup.setData(data);
                popup.setOnDismissListener(() -> {
                    viewModel.selectedTabPos.set(-1);
                });
                popup.setSureListener(bean -> {
                    viewModel.setOrgParam(bean);
                    binding.tvOrgType.setText(TextUtils.equals(bean.getId(), "0") ? getString(R.string.org_type) : bean.getName());
                    helper.start();
                });
                break;
        }
    }

    @Override
    public void onLazyload() {
        lazyloaded = true;
        helper.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (lazyloaded && needRefresh) {
            needRefresh = false;
            helper.start();
        }
    }

    @Override
    public void search(String keyword) {
        viewModel.setKeyword(keyword);
        if (getUserVisibleHint() && lazyloaded) {
            helper.start();
        } else {
            needRefresh = true;
        }
    }


    @Subscribe
    public void onCaseThumbUp(CaseThumbUpEvent event) {
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getCaseId())) {
                caseBean.setIs_relation(event.isThumbUp() ? 1 : 0);
                caseBean.setLike_number(caseBean.getLike_number() + (event.isThumbUp() ? 1 : -1));
                viewModel.thumbUpObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 1) {
            return;
        }

        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
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
        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
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

}
