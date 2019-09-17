package com.chengzi.app.ui.search.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.OrgListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSearchOrganizationBinding;
import com.chengzi.app.databinding.ItemSearchOrganizationGridLayoutBinding;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.popu.DropDownGridPopup;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.search.DisplayChangeRegistry;
import com.chengzi.app.ui.search.OnDisplayChangeObserver;
import com.chengzi.app.ui.search.viewmodel.SearchOrganizationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索机构
 *
 * @ClassName:SearchOrganizationFragment
 * @PackageName:com.yimei.app.ui.search.fragment
 * @Create On 2019/4/4 0004   14:59
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class SearchOrganizationFragment extends LazyloadFragment<FragmentSearchOrganizationBinding,
        SearchOrganizationViewModel> implements OnDisplayChangeObserver, View.OnClickListener, SearchListener {

    private PagingLoadHelper helper;
    private boolean needRefresh;
    private boolean lazyloaded;
    private DropDownGridPopup gridPopup;

    public SearchOrganizationFragment() {
    }

    public static SearchOrganizationFragment newInstance(String keyword) {
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, keyword);
        SearchOrganizationFragment fragment = new SearchOrganizationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((DisplayChangeRegistry) context).register(this);
    }

    @Override
    public void onDetach() {
        ((DisplayChangeRegistry) getContext()).register(this);
        super.onDetach();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_search_organization;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String keyword = getArguments().getString(Sys.EXTRA);
        viewModel.setKeyword(keyword);

        binding.setListener(this);
        binding.setSelectedTabPos(viewModel.selectedTabPos);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        binding.swipeRefreshView.setPulldownEnable(false);
        binding.swipeRefreshView.setEmptyView(null);

        observe();

        onDisplayChange(viewModel.display.get());

        helper.setStartListener(() -> binding.viewSwitcher.setDisplayedChild(0));
        helper.setEmptyListener(isEmpty -> binding.viewSwitcher.setDisplayedChild(isEmpty ? 2 : 1));

        //  默认智能排序
        viewModel.setSortItem(new DropDownPopup.Item(0, "智能排序", "totalSort", "1"));
        viewModel.categoryList();
        helper.start();

    }

    private void observe() {
        viewModel.orgLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        viewModel.categoryLive.observe(this, navigatorItems -> {

            if (gridPopup != null) {
                gridPopup.setData(navigatorItems);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_good: //  擅长商品
                viewModel.selectedTabPos.set(0);
                if (gridPopup == null) {
                    gridPopup = new DropDownGridPopup(getActivity());
                }
                gridPopup.showAsDropDown(v);

                if (viewModel.categoryLive.getValue() != null) {
                    gridPopup.setData(viewModel.categoryLive.getValue());
                }

                gridPopup.setOnDismissListener(() -> {
                    viewModel.selectedTabPos.set(-1);
                });

                gridPopup.setSureListener(bean -> {
                    String categoryId = bean.getValue();
                    String name = bean.getName();
                    binding.tvBeGoodat.setText(TextUtils.equals("全部", name) ? getString(R.string.Good_goods) : name);
                    viewModel.setCategoryId(categoryId);
                    helper.start();
                });

                break;
            case R.id.layout_sort:   //  排序
                viewModel.selectedTabPos.set(1);
                DropDownPopup popup = new DropDownPopup(getActivity());
                popup.showAsDropDown(v);
                List<DropDownPopup.PopupBean> data = new ArrayList<>();
                data.add(new DropDownPopup.Item(0, "智能排序", "totalSort", "1"));
                data.add(new DropDownPopup.Item(1, "评分最高", "gradeSort", "1"));
                data.add(new DropDownPopup.Item(6, "离我最近", "latelySort", "1"));
                data.add(new DropDownPopup.Item(2, "案例数", "caseSort", "1"));
                data.add(new DropDownPopup.Item(3, "咨询数", "sheetSort", "1"));
                data.add(new DropDownPopup.Item(4, "访问量", "visitSort", "1"));
                data.add(new DropDownPopup.Item(5, "下单量", "orderSort", "1"));
                data.add(new DropDownPopup.Item(7, "预约数", "appointmentSort", "1"));
                popup.setData(data);
                popup.setOnDismissListener(() -> {
                    viewModel.selectedTabPos.set(-1);
                });
                popup.setSureListener(bean -> {
                    viewModel.setSortItem(bean);
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
                    viewModel.setOrgItem(bean);
                    binding.tvOrgType.setText(TextUtils.equals(bean.getId(), "0") ? getString(R.string.org_type) : bean.getName());
                    helper.start();
                });
                break;
        }
    }

    private OrgListAdapter listAdapter = new OrgListAdapter();
    private SearchOrganizationGridAdapter gridAdapter = new SearchOrganizationGridAdapter();


    @Override
    public void onDisplayChange(Display display) {
        viewModel.display.set(display);

        List<Object> originData = null;
        BaseQuickAdapter originAdapter = (BaseQuickAdapter) binding.swipeRefreshView.getRecyclerView().getAdapter();
        if (originAdapter != null) {
            originData = originAdapter.getData();
        }

        if (display == Display.LIST) {
            binding.swipeRefreshView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.swipeRefreshView.setAdapter(listAdapter);
            binding.swipeRefreshView.setBackgroundResource(R.color.color_f4f3f8);
            binding.swipeRefreshView.setPadding(0, 0, 0, 0);
        } else {
            binding.swipeRefreshView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            binding.swipeRefreshView.setAdapter(gridAdapter);
            binding.swipeRefreshView.setBackgroundResource(android.R.color.white);
            binding.swipeRefreshView.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(2), 0);
        }
        if (originAdapter != null) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) binding.swipeRefreshView.getRecyclerView().getAdapter();
            helper.onComplete(originData);
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

    private class SearchOrganizationGridAdapter extends BaseQuickAdapter<HospitalBean, BaseViewHolder>
            implements ClickEventHandler<HospitalBean> {

        public SearchOrganizationGridAdapter() {
            super(R.layout.item_search_organization_grid_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HospitalBean item) {
            ItemSearchOrganizationGridLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, HospitalBean bean) {
            HospitalHomePageActivity.start(view.getContext(), bean.getId());
        }
    }
}
