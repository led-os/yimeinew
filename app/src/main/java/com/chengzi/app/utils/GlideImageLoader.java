package com.chengzi.app.utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height, OnImageResultListener listener) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(width, height);
        Glide.with(activity)
                .load(path)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(width, height);
        Glide.with(activity)
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
    @BindingAdapter({"imageUrl"})
    public static void GlideLoadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}