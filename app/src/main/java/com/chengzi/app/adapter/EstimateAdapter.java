package com.chengzi.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemEstimateLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.bean.EvaluateBean;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

public class EstimateAdapter extends BaseQuickAdapter<EvaluateBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {

    private int evaluateType;   //  评价类型 1 商品 ，2医生 ，3 咨询师， 4 医院
    private boolean showPics = true;   //  详情页的评价不显示图片

    public void setShowPics(boolean showPics) {
        this.showPics = showPics;
    }

    private RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();

    public EstimateAdapter(int evaluateType) {
        super(R.layout.item_estimate_layout);
        this.evaluateType = evaluateType;
        pool.setMaxRecycledViews(0, 30);
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluateBean item) {

        Context context = helper.itemView.getContext();

        ItemEstimateLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

        binding.setBean(item);

        if (evaluateType == 1 || evaluateType == 4) {  //  商品的评价 UI 和医院主页 评价的UI一致
            binding.doctorLayout.setVisibility(View.GONE);
            binding.goodsLayout.setVisibility(View.VISIBLE);

            int denominator = 0;
            float gradeCount = 0;

            binding.llDoctorScore.setVisibility(TextUtils.isEmpty(item.getDoctor_id()) || TextUtils.equals(item.getDoctor_id(), "0") ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(item.getDoctor_id()) && !TextUtils.equals(item.getDoctor_id(), "0")) {
                gradeCount += TextUtils.isEmpty(item.getDoctor_skill()) ? 0 : Float.valueOf(item.getDoctor_skill());
                gradeCount += TextUtils.isEmpty(item.getDoctor_major()) ? 0 : Float.valueOf(item.getDoctor_major());
                denominator += 2;
            }

            binding.llCounselorSocre.setVisibility(TextUtils.isEmpty(item.getConsultant_id()) || TextUtils.equals(item.getConsultant_id(), "0") ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(item.getConsultant_id()) && !TextUtils.equals(item.getConsultant_id(), "0")) {
                gradeCount += TextUtils.isEmpty(item.getConsultant_service()) ? 0 : Float.valueOf(item.getConsultant_service());
                gradeCount += TextUtils.isEmpty(item.getConsultant_major()) ? 0 : Float.valueOf(item.getConsultant_major());
                denominator += 2;
            }

            binding.ratingBarAverage.setRating(denominator > 0 ? (gradeCount / denominator) : 0);

        } else {
            binding.doctorLayout.setVisibility(View.VISIBLE);
            binding.goodsLayout.setVisibility(View.GONE);

            if (evaluateType == 2) {

//                binding.tvHint0.setText(context.getString(R.string.profession));
//                binding.tvHint1.setText(context.getString(R.string.skill));
                //审美 技术
                binding.tvHint0.setText(context.getString(R.string.beauty));
                binding.tvHint1.setText(context.getString(R.string.skill));

                binding.ratingBarProfession.setRating(TextUtils.isEmpty(item.getDoctor_major()) ? 0 : Float.valueOf(item.getDoctor_major()));
                binding.ratingBarService.setRating(TextUtils.isEmpty(item.getDoctor_skill()) ? 0 : Float.valueOf(item.getDoctor_skill()));

                binding.tvProfessionScore.setText(TextUtils.isEmpty(item.getDoctor_major()) ? "0.0" : item.getDoctor_major());
                binding.tvServiceScore.setText(TextUtils.isEmpty(item.getDoctor_skill()) ? "0.0" : item.getDoctor_skill());

            } else if (evaluateType == 3) {
                // 审美 服务
                binding.tvHint0.setText(context.getString(R.string.beauty));
                binding.tvHint1.setText(context.getString(R.string.service));

                binding.ratingBarProfession.setRating(TextUtils.isEmpty(item.getConsultant_major()) ? 0 : Float.valueOf(item.getConsultant_major()));
                binding.ratingBarService.setRating(TextUtils.isEmpty(item.getConsultant_service()) ? 0 : Float.valueOf(item.getConsultant_service()));

                binding.tvProfessionScore.setText(TextUtils.isEmpty(item.getConsultant_major()) ? "0.0" : item.getConsultant_major());
                binding.tvServiceScore.setText(TextUtils.isEmpty(item.getConsultant_service()) ? "0.0" : item.getConsultant_service());

            }

        }

        List<String> image = item.getImage();
        if (image == null || image.isEmpty() || !showPics) {
            binding.nineGridView.setVisibility(View.GONE);
        } else {
            binding.nineGridView.setVisibility(View.VISIBLE);

            NineImageAdapter imageAdapter = (NineImageAdapter) binding.nineGridView.getAdapter();
            if (imageAdapter == null) {
                imageAdapter = new NineImageAdapter();
                imageAdapter.setOnItemClickListener(this);
                binding.nineGridView.setRecycledViewPool(pool);
            }
            binding.nineGridView.swapAdapter(imageAdapter, false);
            imageAdapter.setNewData(image);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        NineImageAdapter imageAdapter = (NineImageAdapter) adapter;
        List<String> data = imageAdapter.getData();
        view.getContext().startActivity(new Intent(view.getContext(), PictureSwitcherActivity.class)
                .putExtra(PICTURE_URLS, new ArrayList<>(data))
                .putExtra(PICTURE_INDEX, position));
    }
}
