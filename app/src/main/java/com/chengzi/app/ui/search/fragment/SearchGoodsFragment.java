package com.chengzi.app.ui.search.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.GoodsGridAdapter;
import com.chengzi.app.adapter.GoodsListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSearchGoodsBinding;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.search.DisplayChangeRegistry;
import com.chengzi.app.ui.search.OnDisplayChangeObserver;
import com.chengzi.app.ui.search.activity.AllGoodsActivity;
import com.chengzi.app.ui.search.viewmodel.SearchGoodsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索商品--结果页
 *
 * @ClassName:SearchGoodsFragment
 * @PackageName:com.yimei.app.ui.search.fragment
 * @Create On 2019/4/4 0004   14:55
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class SearchGoodsFragment extends BaseFragment<FragmentSearchGoodsBinding, SearchGoodsViewModel>
        implements OnDisplayChangeObserver, View.OnClickListener, SearchListener {

    public static final int REQUEST_CODE_SELECT_GOODS = 0x03;//选择案例类型
    private PagingLoadHelper helper;
    private boolean needRefresh;

    public SearchGoodsFragment() {
    }

    public static SearchGoodsFragment newInstance(String keyword) {
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, keyword);
        SearchGoodsFragment fragment = new SearchGoodsFragment();
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
        return R.layout.fragment_search_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String keyword = getArguments().getString(Sys.EXTRA);
        viewModel.setKeyword(keyword);

        binding.setListener(this);
        binding.setSelectedTabPos(viewModel.selectedTabPos);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        binding.swipeRefreshView.setEmptyView(null);
        binding.swipeRefreshView.setPulldownEnable(false);
        onDisplayChange(viewModel.display.get());

        viewModel.goodsListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        helper.setEmptyListener(empty -> binding.viewSwitcher.setDisplayedChild(empty ? 2 : 1));
        helper.setStartListener(() -> binding.viewSwitcher.setDisplayedChild(0));

        helper.start();
    }

    private GoodsListAdapter listAdapter = new GoodsListAdapter();
    private GoodsGridAdapter gridAdapter = new GoodsGridAdapter();

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
            binding.swipeRefreshView.setPadding(0, 0, 0, 0);
            binding.swipeRefreshView.setBackgroundResource(R.color.color_f4f3f8);
        } else {
            binding.swipeRefreshView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            binding.swipeRefreshView.setAdapter(gridAdapter);
            binding.swipeRefreshView.setBackgroundResource(android.R.color.white);
            binding.swipeRefreshView.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(2), 0);
        }

        if (originAdapter == null || originData == null) {
//            helper.start();
        } else {
            BaseQuickAdapter adapter = (BaseQuickAdapter) binding.swipeRefreshView.getRecyclerView().getAdapter();
            helper.onComplete(originData);
        }

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
                data.add(new DropDownPopup.Item(1, "评分最高", "grade", "1"));
                data.add(new DropDownPopup.Item(2, "销量最高", "orderNum", "1"));
                data.add(new DropDownPopup.Item(3, "价格最低", "price", "2"));
                data.add(new DropDownPopup.Item(4, "价格最高", "price", "1"));
                data.add(new DropDownPopup.Item(5, "案例最多", "caseNote", "1"));
                data.add(new DropDownPopup.Item(6, "离我最近", "latelySort", "1"));
                data.add(new DropDownPopup.Item(7, "评价最多", "evaluation", "1"));
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && needRefresh) {
            needRefresh = false;
            helper.start();
        }
    }

    @Override
    public void search(String keyword) {
        viewModel.setKeyword(keyword);
        if (!getUserVisibleHint()) {
            needRefresh = true;
        } else {
            helper.start();
        }
    }
}
