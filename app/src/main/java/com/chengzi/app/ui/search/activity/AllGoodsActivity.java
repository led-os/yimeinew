package com.chengzi.app.ui.search.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.adapter.AppBaseAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAllGoodsBinding;
import com.chengzi.app.databinding.ItemAllGoodsHeaderLayoutBinding;
import com.chengzi.app.databinding.ItemAllGoodsTypeLayoutBinding;
import com.chengzi.app.databinding.ItemGoodsTopClassLayoutBinding;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.search.viewmodel.AllGoodsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部商品
 *
 * @ClassName:AllGoodsActivity
 * @PackageName:com.yimei.app.ui.search.activity
 * @Create On 2019/4/3 0003   17:45
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class AllGoodsActivity extends BaseActivity<ActivityAllGoodsBinding, AllGoodsViewModel>
        implements View.OnClickListener {

    private TopClassAdapter topClassAdapter;
    private SecondClassAdapter secondClassAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_all_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        boolean onlyThirdCategory = getIntent().getBooleanExtra(Sys.EXTRA, false);
        mViewModel.setOnlyThirdCategory(onlyThirdCategory);

        boolean forCategory = getIntent().getBooleanExtra(Sys.EXTRA_FOR_CATEGORY, false);
        mBinding.topBar.setCenterText(forCategory ? getString(R.string.all_category_type) : getString(R.string.all_goods_type));

        boolean opCategory = getIntent().getBooleanExtra(Sys.EXTRA_OP_CATEGORY, false);
        mViewModel.setOpCategory(opCategory);

        mBinding.setListener(this);

        initTopClassRecycler();
        initSecondClassRecycler();

        mViewModel.goodsCategoryLiveData.observe(this, CategoryItems -> {

            if (CategoryItems == null || CategoryItems.isEmpty()) {
                mBinding.viewSwitcher.setDisplayedChild(2);
                return;
            }

            if (!mViewModel.isOnlyThirdCategory()) {

                CategoryItem category = new CategoryItem();
                category.setName("全部项目");
                CategoryItem category2 = new CategoryItem();
                category2.setName("全部商品");
                List<CategoryItem> allChildren = new ArrayList<>();
                allChildren.add(category2);
                category.setChildren(allChildren);
                CategoryItems.add(0, category);

            }

            mBinding.viewSwitcher.setDisplayedChild(1);

            topClassAdapter.setNewData(CategoryItems);

            CategoryItem CategoryItem = CategoryItems.get(0);
            mViewModel.setCategory(CategoryItem);
            List<CategoryItem> children = CategoryItem.getChildren();
            secondClassAdapter.clear();
            secondClassAdapter.addData(children);

        });

        mViewModel.loadCategory();

    }

    //  一级分类列表
    private void initTopClassRecycler() {
        topClassAdapter = new TopClassAdapter();
        topClassAdapter.setHasStableIds(true);
        mBinding.recyclerTopClass.setAdapter(topClassAdapter);
        topClassAdapter.setOnItemClickListener((adapter1, view, position) -> {

            mViewModel.selectedPos.set(position);
            CategoryItem CategoryItem = topClassAdapter.getData().get(position);
            mViewModel.setCategory(CategoryItem);

            //刷新二级分类和三级分类
            List<CategoryItem> children = CategoryItem.getChildren();
            secondClassAdapter.clear();
            secondClassAdapter.addData(children);
        });
    }

    //  二三级分类列表
    private void initSecondClassRecycler() {
        secondClassAdapter = new SecondClassAdapter();
        secondClassAdapter.setHasStableIds(true);
        mBinding.recyclerSecondClass.setItemAnimator(null);
        mBinding.recyclerSecondClass.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerSecondClass.setAdapter(secondClassAdapter);

        secondClassAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            if (mViewModel.isOnlyThirdCategory()) {
                return;
            }

            switch (view.getId()) {
                case R.id.textView:
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    CategoryItem secondaryClassificationEntity = secondClassAdapter.getData().get(position);
                    mViewModel.setCategory2(secondaryClassificationEntity);

                    bundle.putString("first_id", mViewModel.getCategory().getId());
                    bundle.putString("second_id", mViewModel.getCategory2().getId());
                    bundle.putString("third_id", null);

                    bundle.putString("categoryName", mViewModel.getCategory2().getName());
                    bundle.putInt("level", 2);

                    intent.putExtras(bundle);
                    setResult(200, intent);
                    finish();
                    break;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load_again:
                mBinding.viewSwitcher.setDisplayedChild(0);
                mViewModel.loadCategory();
                break;
        }
    }

    private class TopClassAdapter extends BaseQuickAdapter<CategoryItem, BaseViewHolder> {

        public TopClassAdapter() {
            super(R.layout.item_goods_top_class_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CategoryItem item) {
            ItemGoodsTopClassLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setPos(helper.getAdapterPosition());
            binding.setSelectedPos(mViewModel.selectedPos);
            binding.setBean(item);
        }
    }

    private class SecondClassAdapter extends AppBaseAdapter<CategoryItem> {

        public SecondClassAdapter() {
            super(R.layout.item_all_goods_header_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CategoryItem item) {
            ItemAllGoodsHeaderLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setShowDrawable(mViewModel.isOnlyThirdCategory());
            helper.addOnClickListener(R.id.textView);

            binding.mRecyclerView.setItemAnimator(null);
            ThirdClassAdapter adapter = (ThirdClassAdapter) binding.mRecyclerView.getAdapter();
            if (adapter == null) {
                adapter = new ThirdClassAdapter();
                binding.mRecyclerView.setAdapter(adapter);
            }

            adapter.clear();
            adapter.addData(item.getChildren());

            adapter.setOnItemClickListener(((adapter1, view, position) -> {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                CategoryItem threeClassificationEntity = (CategoryItem) adapter1.getData().get(position);
                mViewModel.setCategory2(item);
                mViewModel.setCategory3(threeClassificationEntity);

                bundle.putString("first_id", mViewModel.getCategory().getId());
                bundle.putString("second_id", mViewModel.getCategory2().getId());
                bundle.putString("third_id", mViewModel.getCategory3().getId());

                bundle.putString("categoryName", mViewModel.getCategory3().getName());
                bundle.putInt("level", 3);

                intent.putExtras(bundle);
                setResult(200, intent);
                finish();
            }));
        }
    }

    private class ThirdClassAdapter extends AppBaseAdapter<CategoryItem> {
        public ThirdClassAdapter() {
            super(R.layout.item_all_goods_type_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CategoryItem item) {
            ItemAllGoodsTypeLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.textView.setText(item.getName());
            binding.executePendingBindings();

        }
    }
}