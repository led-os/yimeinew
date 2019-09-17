package com.chengzi.app.ui.mine.activity.popularize;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAdvPromoteSnapUpBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.mine.bean.PromotionSelectProductBean;
import com.chengzi.app.ui.mine.viewmodel.AdvPromoteViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.OrderStatusHelp;

import java.util.List;

/**
 * 个人中心 医院 我要推广
 * ① banner广告推广 抢广告位  抢推广
 * ② 专区推广(普通区/VIP区)
 *
 * @ClassName:AdvPromoteSnapUpActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/13 0013   11:28
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class AdvPromoteSnapUpActivity extends BaseActivity<ActivityAdvPromoteSnapUpBindingImpl, AdvPromoteViewModel> {

    //分类id
    private String cate_id;
    private String cate_name;
    //专区类型 （1.普通;2.VIP）
    private String zone_type;
    //类型 1-banner推广 2-商品推广
    private String c_type;

    //商品推广专用-->position
    private int position;

    //上传图片需要传的参数
    private String banner_img;
    private PromotionSelectProductBean bean;


    private Intent ZonePromoteFragmentIntnet;

    /**
     * @param context
     * @param cate_id   分类id
     * @param zone_type 专区类型 （1.普通;2.VIP）
     * @param c_type    类型 1-banner推广 2-商品推广
     * @param cate_name 分类name
     */
    public static void start(Context context, String cate_id, String zone_type, String c_type, String cate_name) {
        context.startActivity(new Intent(context, AdvPromoteSnapUpActivity.class)
                .putExtra("cate_id", cate_id)
                .putExtra("zone_type", zone_type)
                .putExtra("c_type", c_type)
                .putExtra("cate_name", cate_name)
        );
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_adv_promote_snap_up;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        //分类id name
        ZonePromoteFragmentIntnet = getIntent();
        cate_id = getIntent().getStringExtra("cate_id");
        cate_name = getIntent().getStringExtra("cate_name");
        //专区类型 （1.普通;2.VIP）
        zone_type = getIntent().getStringExtra("zone_type");
        c_type = getIntent().getStringExtra("c_type");
        position = getIntent().getIntExtra("position", 0);

        //定位
        String city_name = AccountHelper.getCity_Name();
        if (!TextUtils.isEmpty(city_name))
            mBinding.tvCity.setText(city_name);
        //区(zone_type 专区类型 （1.普通;2.VIP）) + 分类name
        String type = "";
        if (zone_type.equals("1"))
            type = "普通区";
        else if (zone_type.equals("2"))
            type = "VIP区";
        mBinding.tvTypeCate.setText(type + " " + cate_name);

        mBinding.topBar.setCenterText(c_type.equals("1") ? "抢广告位" : "抢推广");
        mBinding.tvImgText.setText(c_type.equals("1") ? "点击加号设置广告位照片，建议宽高比为710：280" : "点击选择商品");
        mBinding.elType.setContent(c_type.equals("1") ? "机构主页" : "商品详情页");
        mBinding.tvHint.setVisibility(c_type.equals("1") ? View.VISIBLE : View.GONE);

        observe();
    }

    private void observe() {
        //添加广告位
        mViewModel.addPromotionLiveData.observe(this, bannerPromotionListBean -> {
            dismissLoading();
            if (bannerPromotionListBean != null && bannerPromotionListBean.isSuccess()) {
                /*/类型 1-banner推广 2-商品推广
    private String c_type;
                * */
                if (c_type.equals("1")) {   //广告推广
                    //刷新 列表页数据
                    toast("广告位添加成功!");
                    OrderStatusHelp.refreshPromoteList(zone_type, cate_id);
                    finish();
                } else {                    //商品推广
                    Intent intent = ZonePromoteFragmentIntnet;
                    Bundle bundle = new Bundle();
//
//                    bundle.putInt("position", position);
//                    bundle.putString("c_type", c_type);
//                    bundle.putString("cate_id", cate_id);
                    bundle.putString("zone_type", zone_type);
//                    bundle.putString("model_id", model_id);
//                    bundle.putSerializable("bean", bean);
//                    bundle.putString("banner_img", banner_img);
                    intent.putExtras(bundle);
                    setResult(200, intent);
                    finish();
                }
            }
        });
    }

    private static final int CHOOSE_GOODS = 0x08;


    //商品id
    private String model_id;
    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.rl_img:      // 广告位照片
                if (c_type.equals("1")) {
                    CertifiedHelp.showDialogWithCrop(AdvPromoteSnapUpActivity.this, false);
                } else {           // 选择商品
                    Intent intent = new Intent(this, ChooseGoodsActivity.class);
                    intent.putExtra("cate_id", cate_id);
                    intent.putExtra("zone_type", zone_type);
                    intent.putExtra("position", position);
                    startActivityForResult(intent, CHOOSE_GOODS);
                }
                break;
            case R.id.tv_confirm:  //确定
                String amount = mBinding.etSeckillMoney.getText().toString();
                //1-banner推广 2-商品推广
                if (c_type.equals("1")) {
                    //模式ID，商品推广时是商品ID，banner推广时是医院ID
                    String model_id = AccountHelper.getUserId();
                    if (TextUtils.isEmpty(banner_img)) {
                        toast("请上传广告位照片!");
                        break;
                    }
                    ////推广限额:如果不为空时 判断是不是小于推广金额
                    if (!TextUtils.isEmpty(amount)) {
                        double v = Double.parseDouble(amount);
                        if (v < IWantPopularizeActivity.promotion_price) {
                            toast(getResources().getString(R.string.want_popularize));
                            return;
                        }
                    }
                    showLoading(Sys.LOADING);
                    if (TextUtils.isEmpty(amount))
                        mViewModel.addPromotion(c_type, cate_id, zone_type, model_id, banner_img);
                    else
                        mViewModel.addPromotion(c_type, cate_id, zone_type, model_id, amount, banner_img);
                } else {
                    if (TextUtils.isEmpty(amount)) {
                        toast("请输入预设消耗额度!");
                        break;
                    }
                    ////推广限额:如果不为空时 判断是不是小于推广金额
                    if (!TextUtils.isEmpty(amount)) {
                        double v = Double.parseDouble(amount);
                        if (v < IWantPopularizeActivity.promotion_price) {
                            toast(getResources().getString(R.string.want_popularize));
                            return;
                        }
                    }
                    showLoading(Sys.LOADING);
                    mViewModel.addPromotion(c_type, cate_id, zone_type, model_id, amount, banner_img);
                }
                break;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Sys.REQUEST_CODE_PIC:  //上传图片
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                showLoading(Sys.LOADING);
                AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), picPath, new AliUpload.CallBack() {
                    @Override
                    public void onSuccess(String paths) {
                        dismissLoading();
                        banner_img = paths;
                        ImageLoader.loadImage(mBinding.ivImg, paths);
                        mBinding.tvImgText.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(AliError errors) {
                        dismissLoading();
                        Log.d("ALI_UP", "onError: " + errors.toString());
                        toast("上传失败");
                    }
                });
                break;
            case CHOOSE_GOODS:  // 选择商品
                Bundle bundle = data.getExtras();
                model_id = bundle.getString("model_id");    //商品id
                cate_id = bundle.getString("cate_id");      //商品分类
                String goods_image = bundle.getString("goods_image");   //商品图片
                position = bundle.getInt("position");
                bean = (PromotionSelectProductBean) data.getSerializableExtra("data");
                ImageLoader.loadImage(mBinding.ivImg, goods_image);
                break;
        }
    }
}
