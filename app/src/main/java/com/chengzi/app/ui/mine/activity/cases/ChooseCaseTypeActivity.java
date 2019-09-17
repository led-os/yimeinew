package com.chengzi.app.ui.mine.activity.cases;

import android.support.annotation.Nullable;
import android.os.Bundle;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityChooseCaseTypeBinding;

/**
 * 选择案例类型
 *
 * @ClassName:ChooseCaseTypeActivity
 * @PackageName:com.yimei.app.ui.mine.activity.cases
 * @Create On 2019/4/17 0017   15:11
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/17 0017 handongkeji All rights reserved.
 */
public class ChooseCaseTypeActivity extends BaseActivity<ActivityChooseCaseTypeBinding, BaseViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_choose_case_type;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
//        initTopClassRecycler();
//        initSecondClassRecycler();
    }

    //  一级分类列表
   /* private void initTopClassRecycler() {
        TopClassAdapter adapter = new TopClassAdapter();
        mBinding.recyclerTopClass.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

        });

        List<String> provinces = new ArrayList<>();
        provinces.add("眼部整形");
        provinces.add("鼻部整形");
        provinces.add("胸部整形");
        provinces.add("面部轮廓");
        provinces.add("脂肪整形");
        provinces.add("眉部整形");
        provinces.add("毛发美容");
        provinces.add("口唇整形");
        provinces.add("牙齿整形");
        provinces.add("耳部整形");
        adapter.addData(provinces);
    }  //  二三级分类列表
    private void initSecondClassRecycler() {
        SecondClassAdapter adapter = new SecondClassAdapter();
        adapter.setMultiTypeDelegate(new GoodsClassMultiTypeDelegate());
        adapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            if (adapter.getItem(position).getItemType() == 1) {
                return 2;
            } else {
                return 1;
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mBinding.recyclerSecondClass.setLayoutManager(gridLayoutManager);
        mBinding.recyclerSecondClass.setAdapter(adapter);

        List<AppNavigatorActivity.AppNavigatorItem> list = new ArrayList<>();

        list.add(new AppNavigatorActivity.AppNavigatorItem(1, "面部年轻化", null));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "抗衰老针"));

        list.add(new AppNavigatorActivity.AppNavigatorItem(1, "祛斑", null));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "微晶削磨祛斑"));

        list.add(new AppNavigatorActivity.AppNavigatorItem(1, "嫩肤美白", null));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));
        list.add(new AppNavigatorActivity.AppNavigatorItem(0, "美白针"));

        adapter.setNewData(list);


        adapter.setOnItemClickListener(((adapter1, view, position) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("name", adapter.getData().get(position).getTypeText());
            intent.putExtras(bundle);
            setResult(200, intent);
            finish();
        }));

    }*/

//    private class TopClassAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
//
//        public TopClassAdapter() {
//            super(R.layout.item_choose_case_type_layout);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, Object item) {
//            ItemChooseCaseTypeLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
//            binding.setPos(helper.getAdapterPosition());
//            binding.setBean(item);
//        }
//    }
//
//    private class SecondClassAdapter extends BaseQuickAdapter<AppNavigatorActivity.AppNavigatorItem, BaseViewHolder> {
//
//        public SecondClassAdapter() {
//            super(0);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, AppNavigatorActivity.AppNavigatorItem item) {
//            if (helper.getItemViewType() == 0) {
//                ItemAllGoodsTypeLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
//                binding.setBean(item);
//            } else {
//                ItemAllGoodsHeaderLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
//                binding.textView.setText(item.getTypeText());
//                binding.executePendingBindings();
//
//                binding.textView.setCompoundDrawables(null, null, null, null);//设置
//            }
//        }
//    }
//
//    private class GoodsClassMultiTypeDelegate extends MultiTypeDelegate<AppNavigatorActivity.AppNavigatorItem> {
//
//        public GoodsClassMultiTypeDelegate() {
//            registerItemTypeAutoIncrease(R.layout.item_all_goods_type_layout,
//                    R.layout.item_all_goods_header_layout);
//        }
//
//        @Override
//        protected int getItemType(AppNavigatorActivity.AppNavigatorItem o) {
//            return o.getItemType();
//        }
//    }
}
