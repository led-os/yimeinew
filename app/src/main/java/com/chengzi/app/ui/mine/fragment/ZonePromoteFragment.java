package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentZonePromoteBindingImpl;
import com.chengzi.app.databinding.ItemZonePromoteLayoutBindingImpl;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.mine.activity.popularize.AdvPromoteSnapUpActivity;
import com.chengzi.app.ui.mine.bean.ZonePromotionBean;
import com.chengzi.app.ui.mine.viewmodel.AdvPromoteViewModel;

import java.util.List;

/**
 * 专区推广 普通区推广/vip推广
 *
 * @ClassName:ZonePromoteFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/15 0015   10:27
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class ZonePromoteFragment extends BaseFragment<FragmentZonePromoteBindingImpl, AdvPromoteViewModel> {
    //专区类型 1.普通;2.VIP
    private String zone_type;
    private ZonePromoteAdapter adapter;

    //类型 1-banner推广 2-商品推广
    private String c_type = "2";
    //【 必填】专区（分类）ID
    private String cate_id;
    private String cate_name;

    //选择商品
    private static final int CHOOSE_GOODS = 0x08;

    //专区类型 1.普通;2.VIP
    public static ZonePromoteFragment newInstance(String zone_type) {
        ZonePromoteFragment fragment = new ZonePromoteFragment();
        Bundle args = new Bundle();
        args.putString("zone_type", zone_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_zone_promote;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            zone_type = arguments.getString("zone_type");
        }
        adapter = new ZonePromoteAdapter();
        binding.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mRecyclerView.setAdapter(adapter);

        observe();
    }

    private void observe() {
        showLoading(Sys.LOADING);
        viewModel.zonePromotion(zone_type);
        //列表
        viewModel.zonePromotionLiveData.observe(this, objects -> {
            dismissLoading();
            if (objects != null && objects.getData() != null) {
                ZonePromotionBean data = objects.getData();
                if (data.getCategory_list() != null && data.getCategory_list().size() > 0) {
                    List<ZonePromotionBean.CategoryListEntity> category_list = data.getCategory_list();
                    adapter.setNewData(category_list);
                } else {
                    binding.mRecyclerView.setVisibility(View.GONE);
                    binding.emptyView.setVisibility(View.VISIBLE);
                }
                ZonePromotionBean.UserInfoEntity userInfo = data.getUserInfo();
                if (userInfo != null) {
                    if (!TextUtils.isEmpty(userInfo.getCity_name()))
                        binding.tvCity.setText(userInfo.getCity_name());
                }
            } else {
                binding.mRecyclerView.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            }
        });

        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            ZonePromotionBean.CategoryListEntity bean = adapter.getData().get(position);
            cate_id = bean.getCategoryId();
            cate_name = bean.getCategoryName();
            switch (view.getId()) {
                case R.id.tv_skin_beauty_zone_promote:    //抢推广
                    Intent intent = new Intent(getContext(), AdvPromoteSnapUpActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("cate_id", cate_id);
                    intent.putExtra("zone_type", zone_type);
                    intent.putExtra("c_type", c_type);
                    intent.putExtra("cate_name", cate_name);
                    startActivityForResult(intent, CHOOSE_GOODS);
                    break;
                case R.id.tv_revocation:    //专区推广撤销
                    showLoading(Sys.LOADING);
//                    toast("撤销商品推广!");
//                    viewModel.removeSearchPromotionItem(bean.getGood_lists().getId(), "1");
                    viewModel.removePromotion(bean.getGood_lists().getExtension_id());
                    break;
                case R.id.rl_skin_beauty_zone_promote:    //商品详情
                    if (bean.getGood_list() != null && bean.getGood_list().size() > 0) {
                        GoodsDetailActivity.start(getContext(), bean.getGood_lists().getId());
                    }
                    break;
            }
        });
        //撤销商品
        viewModel.removePromotionLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                viewModel.zonePromotion(zone_type);
            } else {
                dismissLoading();
            }
        });
    }

    public class ZonePromoteAdapter extends BaseQuickAdapter<ZonePromotionBean.CategoryListEntity, BaseViewHolder> {
        public ZonePromoteAdapter() {
            super(R.layout.item_zone_promote_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ZonePromotionBean.CategoryListEntity item) {
            ItemZonePromoteLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            //// 该专区已有推广商品
            List<ZonePromotionBean.CategoryListEntity.GoodListEntity> good_list = item.getGood_list();
            //// 当前用户是否能抢该推广位
            boolean canCatch = item.isCanCatch();
            binding.setBean(item);
//            if (canCatch) { //可抢
//                binding.tvSkinBeautyZonePromote.setVisibility(View.VISIBLE);
//                binding.rlSkinBeautyZonePromote.setVisibility(View.GONE);
//            } else {        //不可抢 显示
//                if (good_list != null && good_list.size() > 0) {
//                    binding.tvSkinBeautyZonePromote.setVisibility(View.GONE);
//                    binding.rlSkinBeautyZonePromote.setVisibility(View.VISIBLE);
//                    //显示商品
//                    ZonePromotionBean.CategoryListEntity.GoodListEntity goodListEntity = good_list.get(0);
//                    if (goodListEntity != null) {
//                        binding.setUrl(goodListEntity.getImage());
//                    }
//                } else {
//                    binding.tvSkinBeautyZonePromote.setVisibility(View.VISIBLE);
//                    binding.rlSkinBeautyZonePromote.setVisibility(View.GONE);
//                }
//            }
            if (good_list == null || good_list.size() <= 0) { //可抢
                binding.tvSkinBeautyZonePromote.setVisibility(View.VISIBLE);
                binding.rlSkinBeautyZonePromote.setVisibility(View.GONE);
            } else {        //不可抢 显示
                binding.tvSkinBeautyZonePromote.setVisibility(View.GONE);
                binding.rlSkinBeautyZonePromote.setVisibility(View.VISIBLE);
                //显示商品
                ZonePromotionBean.CategoryListEntity.GoodListEntity goodListEntity = good_list.get(0);
                if (goodListEntity != null) {
                    binding.setUrl(goodListEntity.getImage());
                }
            }
            binding.executePendingBindings();
            //推广 撤销 商品详情
            helper.addOnClickListener(R.id.tv_skin_beauty_zone_promote)
                    .addOnClickListener(R.id.tv_revocation)
                    .addOnClickListener(R.id.rl_skin_beauty_zone_promote);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case CHOOSE_GOODS:
                Bundle bundle = data.getExtras();
//                ProductPromotionProductBean bean = (ProductPromotionProductBean) bundle.getSerializable("bean");
//                int position = bundle.getInt("position");
//                c_type = bundle.getString("c_type");
//                cate_id = bundle.getString("cate_id");
//                zone_type = bundle.getString("zone_type");
//                model_id = bundle.getString("model_id");

                String zone_type1 = bundle.getString("zone_type");
                //刷新-->
                if (zone_type.equals(zone_type1)) {
                    showLoading(Sys.LOADING);
                    viewModel.zonePromotion(zone_type);
                }
                break;
        }
    }
}
