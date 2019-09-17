package com.chengzi.app.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandLayoutMultipleImageBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.common.activity.VideoPlayActivity;
import com.chengzi.app.ui.discover.bean.ForumBean;

import java.util.ArrayList;
import java.util.List;

public class MultipleImageLayout extends FrameLayout {

    private ViewGroup parent;
    private ImageView imageSingle;
    private HandLayoutMultipleImageBinding binding;
    private ImageAdapter imageAdapter;

    public MultipleImageLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MultipleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultipleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MultipleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.hand_layout_multiple_image, this, true);
        binding.setListener(mOnClickListener);
        parent = findViewById(R.id.container);
        imageSingle = findViewById(R.id.single_image);
        imageAdapter = new ImageAdapter();
        binding.ninegrid.setAdapter(imageAdapter);
        binding.ninegrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition % 3 == 0) {
                    outRect.set(0, 0, SizeUtils.dp2px(6), SizeUtils.dp2px(10));
                } else if (childAdapterPosition % 3 == 1) {
                    outRect.set(SizeUtils.dp2px(3), 0, SizeUtils.dp2px(3), SizeUtils.dp2px(10));
                } else {
                    outRect.set(SizeUtils.dp2px(6), 0, 0, SizeUtils.dp2px(10));
                }
            }
        });
        imageAdapter.setOnItemClickListener((adapter, view, position) -> {
            PictureSwitcherActivity.start(view.getContext(), (ArrayList<String>) imageAdapter.getData(), position);
        });
    }

    private ForumBean bean;

    public void setBean(ForumBean bean) {

        if (this.bean != null && bean != null && this.bean.equals(bean)) {
            return;
        }

        this.bean = bean;
        ViewGroup root = (ViewGroup) binding.getRoot();
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            root.getChildAt(i).setVisibility(View.GONE);
        }
        if (bean == null || ((bean.getPics() == null || bean.getPics().isEmpty()) && TextUtils.isEmpty(bean.getVideo()))) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        if (TextUtils.equals(bean.getResource_type(), "2")) {
            binding.videoImage.setVisibility(VISIBLE);
            binding.btnPlay.setVisibility(VISIBLE);
            ImageLoader.loadImage(binding.videoImage, bean.getVideo(), 0, R.color.colorGray);
        } else {
            binding.ninegrid.setVisibility(VISIBLE);
            imageAdapter.setNewData(bean.getPics());
        }
    }

    public void setImages(List<String> list) {

        if (list == null || list.isEmpty()) {
            return;
        }
        if (list.size() == 1) {
            imageSingle.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(imageSingle, list.get(0), R.color.colorGray);
        } else if (list.size() >= 2) {
            binding.ninegrid.setVisibility(View.VISIBLE);
            imageAdapter.setNewData(list);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (v.getId()) {
                case R.id.btn_play:
                case R.id.video_image:
                    VideoPlayActivity.start(v.getContext(), bean.getVideo(), "");
                    break;
            }

        }
    };

    private static class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ImageAdapter() {
            super(R.layout.item_image3_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageLoader.loadImage(helper.getView(R.id.iv_img), item, 0, R.color.colorGray);
        }
    }
}
