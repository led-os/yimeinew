package com.chengzi.app.databindingadapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;


public class ImageViewBindingAdapters {

    @BindingAdapter(value = {"image", "avatar"}, requireAll = false)
    public static void setImage(ImageView imageView, String path, boolean avatar) {

        if (avatar) {
            if (TextUtils.isEmpty(path)) {
                imageView.setImageResource(R.drawable.morentouxiang);
            } else {
                ImageLoader.loadImage(imageView, path, 0, R.drawable.morentouxiang);
            }
        } else {
            if (TextUtils.isEmpty(path)) {
                imageView.setImageResource(R.color.colorGray);
            } else {
                ImageLoader.loadImage(imageView, path, 0, R.color.colorGray);
            }
        }
    }
}
