package com.chengzi.app.ui.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivityVideoPlayBinding;
import com.chengzi.app.utils.ProxyHelper;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity<HandActivityVideoPlayBinding, BaseViewModel> {

    public static final String VIDEO_PATH = "VIDEO_PATH";
    public static final String VIDEO_NAME = "VIDEO_NAME";
    public static final String VIDEO_CUT = "VIDEO_CUT";

    private static final int REQUEST_CODE_TRIMMER = 0x01;
    private MediaMetadataRetriever retriever;

    public static void start(Context context, String videoUrl, String videoName) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(VIDEO_PATH, videoUrl)
                .putExtra(VIDEO_NAME, videoName);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.hand_activity_video_play;
    }

    @Override
    public void initView(Bundle bundle) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator((R.drawable.hand_back_white));

        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra(VIDEO_PATH);
        String videoName = intent.getStringExtra(VIDEO_NAME);

        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        boolean forCut = intent.getBooleanExtra(VIDEO_CUT, false);

        mBinding.llForCut.setVisibility(forCut ? View.VISIBLE : View.GONE);
        mBinding.btnCut.setOnClickListener(v -> {

            startActivityForResult(new Intent(this, VideoTrimmerActivity.class)
                    .putExtra("videoPath", videoUrl), REQUEST_CODE_TRIMMER);

        });

        JzvdStd jzvdStd = findViewById(R.id.videoplayer);
        JZUtils.clearSavedProgress(this, videoUrl);
        jzvdStd.setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);

        String newVideoUrl = null;
        if (videoUrl.startsWith("http:")) {
            newVideoUrl = ProxyHelper.getProxy().getProxyUrl(videoUrl, false);
        } else {
            newVideoUrl = videoUrl;
        }

        jzvdStd.setUp(newVideoUrl, videoName, Jzvd.SCREEN_WINDOW_NORMAL);

        ImageLoader.loadImage(jzvdStd.thumbImageView, videoUrl);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_TRIMMER) {
            setResult(resultCode, data);
            finish();
        }
    }

}
