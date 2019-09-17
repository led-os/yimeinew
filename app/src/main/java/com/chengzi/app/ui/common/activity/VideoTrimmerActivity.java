package com.chengzi.app.ui.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivityVideoTrimmerBinding;
import com.chengzi.app.ui.common.mediametadataretriever.adapter.VideoEditAdapter;
import com.chengzi.app.ui.common.mediametadataretriever.bean.VideoEditInfo;
import com.chengzi.app.ui.common.mediametadataretriever.util.ExtractFrameWorkThread;
import com.chengzi.app.utils.VideoUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @ClassName:VideoTrimmerActivity
 * @PackageName:com.hangdongkeji.arcfox.carfans.publish.activity
 * @Create On 2018/12/13 0013   16:44
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2018/12/13 0013 handongkeji All rights reserved.
 */
public class VideoTrimmerActivity extends BaseActivity<HandActivityVideoTrimmerBinding, BaseViewModel> implements View.OnClickListener {

    public static final String SHORTEN_VIDEO_PATH = "shorten_video_path";

    private VideoEditAdapter adapter;
    private ExtractFrameWorkThread mThread;


    @Override
    public int getLayoutRes() {
        return R.layout.hand_activity_video_trimmer;
    }

    @Override
    public void initView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator((R.drawable.hand_back_white));

        mBinding.setListener(this);

        String videoPath = getIntent().getStringExtra("videoPath");
        JZUtils.clearSavedProgress(this, videoPath);
        mBinding.videoplayer.setUrl(videoPath);
        mBinding.videoplayer.startPlay();

        int width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(100)) / 15;
        mBinding.recorderView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new VideoEditAdapter(this, width);
        mBinding.recorderView.setAdapter(adapter);

        ImageLoader.loadImage(mBinding.videoplayer.thumbImageView, videoPath);

        int extractW = width;
        int extractH = SizeUtils.dp2px(55);
        File dir = new File(getExternalCacheDir(), "frame");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long duration = getDuration(videoPath);

        mThread = new ExtractFrameWorkThread(extractW, extractH, handler, videoPath, dir.getAbsolutePath(),
                0, duration);
        mThread.start();

        initRecycler();
    }


    private void initRecycler() {
        mBinding.recorderView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recorderView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    VideoEditInfo item = adapter.getItem(firstVisibleItemPosition);
                    long time = item.time;
                    JZMediaManager.instance().seekTo(time);
                }
            }
        });
    }

    private long getDuration(String fileName) {
        Movie movie = null;
        try {
            movie = MovieCreator.build(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (movie == null) {
            return 0;
        }
        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        double endTime = (double) tracks.get(0).getDuration() / tracks.get(0).getTrackMetaData().getTimescale();
        return (long) (endTime * 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                VideoEditInfo info = (VideoEditInfo) msg.obj;
                adapter.addItemVideoInfo(info);
            }
        }
    };

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
    protected void onDestroy() {
        super.onDestroy();
        if (mThread != null) {
            mThread.stopExtract();
        }
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            onBackPressed();
        } else if (id == R.id.btn_complete) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recorderView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (adapter != null) {
                VideoEditInfo firstItem = adapter.getItem(firstVisibleItemPosition);
                VideoEditInfo lastItem = adapter.getItem(lastVisibleItemPosition);
                if (firstItem != null && lastItem != null) {
                    double startTime = firstItem.time * 1.000 / 1000;
                    double endTime = lastItem.time * 1.000 / 1000;
                    showLoading("");
                    Api.EXECUTOR.execute(() -> {
                        String videoPath = getIntent().getStringExtra("videoPath");

                        File file = new File(SDCardUtils.isSDCardEnableByEnvironment() ? getExternalCacheDir() : getCacheDir(), "video");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File dstFile = new File(file, String.format("shorten-%f-%f", startTime, endTime));
                        boolean isSuccess = false;
                        if (dstFile.exists()) {
                            dstFile.delete();
                        }
                        try {
                            dstFile.createNewFile();
                            VideoUtils.shortenVideo(videoPath, dstFile.getAbsolutePath(), startTime, endTime);
                            isSuccess = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("错误：" + e.getMessage());
                            isSuccess = false;
                        }
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> dismissLoading());
                        if (isSuccess) {
                            handler.post(() -> {
                                Intent intent = new Intent();
                                intent.putExtra(SHORTEN_VIDEO_PATH, dstFile.getAbsolutePath());
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            });
                        }
                    });
                }
            }
        }
    }

}
