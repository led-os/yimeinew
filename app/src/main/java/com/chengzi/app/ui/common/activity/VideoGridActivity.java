package com.chengzi.app.ui.common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.lzy.imagepicker.ui.ImageBaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivityVideoGridBinding;
import com.chengzi.app.ui.common.VideoDataSource;
import com.chengzi.app.ui.common.VideoItem;
import com.chengzi.app.ui.common.VideoRecyclerAdapter;
import com.chengzi.app.widget.GridSpacingItemDecoration;

import java.util.List;

import static com.lzy.imagepicker.ui.ImageGridActivity.REQUEST_PERMISSION_STORAGE;
import static com.chengzi.app.ui.common.activity.VideoPlayActivity.VIDEO_CUT;
import static com.chengzi.app.ui.common.activity.VideoPlayActivity.VIDEO_PATH;

public class VideoGridActivity extends ImageBaseActivity implements VideoDataSource.OnVideosLoadedListener, View.OnClickListener {

    private static final int REQUEST_CODE_TRIMMER = 0x01;

    public static final String VIDEO_ITEM = "video_item";

    private HandActivityVideoGridBinding mBinding;
    private VideoRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.hand_activity_video_grid);
        mBinding.setListener(this);
        initView();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new VideoDataSource(this, this);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            new VideoDataSource(this, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new VideoDataSource(this, this);
            } else {
                showToast("权限被禁止，无法选择本地视频");
            }
        }
    }

    private void initView() {
        mBinding.recycler.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.recycler.addItemDecoration(new GridSpacingItemDecoration(3,
                SizeUtils.dp2px(2), false));
        mAdapter = new VideoRecyclerAdapter(this);
        mBinding.recycler.setAdapter(mAdapter);
    }

    @Override
    public void onVideosLoaded(List<VideoItem> items) {
        mAdapter.refreshData(items);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.btn_ok) {
            VideoItem selectedItem = mAdapter.getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.duration > 11000) {
                    //  裁剪视频
                    startActivityForResult(new Intent(this, VideoPlayActivity.class)
                                    .putExtra(VIDEO_PATH, selectedItem.path)
                                    .putExtra(VIDEO_CUT, true)
                            , REQUEST_CODE_TRIMMER);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(VIDEO_ITEM, selectedItem);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_TRIMMER) {
            String shortenPath = data.getStringExtra(VideoTrimmerActivity.SHORTEN_VIDEO_PATH);

            VideoItem item = new VideoItem();
            item.path = shortenPath;

            data.putExtra(VIDEO_ITEM, item);

            setResult(resultCode, data);
            finish();
        }
    }
}
