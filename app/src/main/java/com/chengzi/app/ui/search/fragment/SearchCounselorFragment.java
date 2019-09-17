package com.chengzi.app.ui.search.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.DoctorListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSearchCounselorBinding;
import com.chengzi.app.databinding.ItemSearchCounselorGridLayoutBinding;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.popu.DropDownGridPopup;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.search.DisplayChangeRegistry;
import com.chengzi.app.ui.search.OnDisplayChangeObserver;
import com.chengzi.app.ui.search.viewmodel.SearchCounselorViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索咨询师
 *
 * @ClassName:SearchCounselorFragment
 * @PackageName:com.yimei.app.ui.search.fragment
 * @Create On 2019/4/4 0004   14:59
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class SearchCounselorFragment extends LazyloadFragment<FragmentSearchCounselorBinding,
        SearchCounselorViewModel> implements OnDisplayChangeObserver, View.OnClickListener, SearchListener {

    private PagingLoadHelper helper;
    private DropDownGridPopup gridPopup;
    private boolean needRefresh;
    private boolean lazyloaded;

    public SearchCounselorFragment() {
    }

    public static SearchCounselorFragment newInstance(String keyword) {
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, keyword);
        SearchCounselorFragment fragment = new SearchCounselorFragment();
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
        return R.layout.fragment_search_counselor;
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

        onDisplayChange(viewModel.display.get());

        helper.setEmptyListener(isEmpty -> binding.viewSwitcher.setDisplayedChild(isEmpty ? 2 : 1));
        helper.setStartListener(() -> binding.viewSwitcher.setDisplayedChild(0));

        observe();

        viewModel.categoryList();
        helper.start();
    }

    private void observe() {
        viewModel.counselorLive.observe(this, objects -> {
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
                data.add(new DropDownPopup.Item(0, "全部", "title", null));
                data.add(new DropDownPopup.Item(1, "普通咨询师", "title", "1"));
                data.add(new DropDownPopup.Item(6, "中等咨询师", "title", "2"));
                data.add(new DropDownPopup.Item(2, "高等咨询师", "title", "3"));
                data.add(new DropDownPopup.Item(3, "VIP咨询师", "title", "4"));
                popup.setData(data);
                popup.setOnDismissListener(() -> {
                    viewModel.selectedTabPos.set(-1);
                });
                popup.setSureListener(bean -> {
                    viewModel.setTitleType(bean);
                    binding.tvTitleType.setText(TextUtils.equals(bean.getName(), "全部") ? getString(R.string.title_classification) : bean.getName());
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
                    viewModel.setOrgType(bean);
                    binding.tvOrgType.setText(TextUtils.equals(bean.getName(), "不限类型") ? getString(R.string.org_type) : bean.getName());
                    helper.start();
                });
                break;
        }
    }

    private DoctorListAdapter listAdapter = new DoctorListAdapter(3);
    private SearchCounselorGridAdapter gridAdapter = new SearchCounselorGridAdapter();


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
        } else {
            binding.swipeRefreshView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            binding.swipeRefreshView.setAdapter(gridAdapter);
            binding.swipeRefreshView.setBackgroundResource(android.R.color.white);
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

    private class SearchCounselorGridAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder>
            implements ClickEventHandler<DoctorBean> {
        int dip10 = SizeUtils.dp2px(10);
        int dip5 = SizeUtils.dp2px(5);
        public SearchCounselorGridAdapter() {
            super(R.layout.item_search_counselor_grid_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            ItemSearchCounselorGridLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setListener(this);
            int adapterPosition = helper.getAdapterPosition();
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.layout.getLayoutParams();
            if (adapterPosition %2 == 0) {
                layoutParams.leftMargin = dip10;
                layoutParams.topMargin = dip10;
                layoutParams.rightMargin = dip5;
                layoutParams.bottomMargin = 0;
            }else{
                layoutParams.leftMargin = dip5;
                layoutParams.topMargin = dip10;
                layoutParams.rightMargin = dip10;
                layoutParams.bottomMargin = 0;
            }
            binding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, DoctorBean bean) {
            DoctorHomePageActivity.startCounselor(view.getContext(), bean.getId());
        }
    }
}
