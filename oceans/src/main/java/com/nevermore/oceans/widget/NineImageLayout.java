package com.nevermore.oceans.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.handongkeji.common.SystemHelper;
import com.handongkeji.utils.DisplayUtil;
import com.nevermore.oceans.R;
import com.nevermore.oceans.uits.CommonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class NineImageLayout extends CellLayout implements CellLayout.OnNewLineCondition {

    public NineImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCondition(this);

    }

    @Override
    public boolean allowNewLine(int columCount, int childindex, int childCount) {
        if (columCount == 3 && childindex == 2 && childCount == 4) {
            return false;
        }
        return true;
    }

    @Override
    public boolean forceNewline(int columCount, int childindex, int childCount) {
        if (columCount == 3 && childindex == 1 && childCount == 4) {
            return true;
        }
        return false;
    }

    private static final String TAG = "NineImageLayout";

    public interface OnGetImagPath<T> {
        String getPath(T t);
    }

    public <T> void setImages(final List<T> imageDatas, OnGetImagPath<T> getImagPath) {
        removeAllViews();
        if (imageDatas == null && imageDatas.size() == 0) {
            setVisibility(GONE);
            return;
        }
        DisplayMetrics dm = SystemHelper.getScreenInfo(getContext());
        int screenWidths = dm.widthPixels;
        int screenHeights = dm.heightPixels;

        int screenWidth = screenWidths - DisplayUtil.dip2px(getContext(), 20);
        Log.i(TAG, "screenWidth: " + screenWidth);


        final ArrayList<String> pics = new ArrayList<>();
        for (T pic : imageDatas) {
            pics.add(getImagPath.getPath(pic));
        }
        if (imageDatas.size() == 1) {
            String path = getImagPath.getPath(imageDatas.get(0));
            Glide.with(getContext())
                    .load(path)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            int height = resource.getIntrinsicHeight();
                            int width = resource.getIntrinsicWidth();
                            Log.i(TAG, "height0: " + height);
                            Log.i(TAG, "width0: " + width);

                            float minSize = screenWidth * 0.5f;

                            if (width > height) {
                                float min = (float) minSize / width;
                                Log.i(TAG, "onResourceReady1: " + min);
                                height *= min;
                                width *= min;
                            } else {
                                float min = minSize / width;
                                Log.i(TAG, "onResourceReady0: " + min);
                                height *= min;
                                width *= min;
                            }

                            Log.i(TAG, "height1: " + height);
                            Log.i(TAG, "width1: " + width);

//                            final ImageView imageView = new ColorFilterImageView(getContext());
//                            imageView.setClickable(true);
                            final ImageView imageView = new ImageView(getContext());
                            imageView.setImageDrawable(resource);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            LayoutParams params = new LayoutParams(width, height);
                            imageView.setLayoutParams(params);
                            addView(imageView);
//                            imageView.setOnClickListener(v -> {
//                                ImageBroseActivity.start(getContext(), pics, 0);
//                            });
                        }
                    });
        } else {
            for (int i = 0; i < imageDatas.size(); i++) {
//                final ImageView imageView = new ColorFilterImageView(getContext());
//                imageView.setClickable(true);
                final ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                T t = imageDatas.get(i);
                String path = getImagPath.getPath(t);
                if (CommonUtils.isStringNull(path)) {
                    imageView.setImageResource(0);
                } else {
                    Glide.with(getContext()).load(path)
                            .into(imageView);
                }
                int position = i;
                addView(imageView);
//                imageView.setOnClickListener(v -> {
//                    ImageBroseActivity.start(getContext(), pics, position);
//                });
            }
        }
    }
}