package com.chengzi.app.ui.discover.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivityPublishCameraBinding;
import com.chengzi.app.recordvideo.listener.ClickListener;
import com.chengzi.app.recordvideo.listener.ErrorListener;
import com.chengzi.app.recordvideo.listener.JCameraListener;
import com.chengzi.app.recordvideo.utils.FileUtil;
import com.chengzi.app.recordvideo.widget.JCameraView;

import java.io.File;


public class PublishCameraActivity extends BaseActivity<HandActivityPublishCameraBinding, BaseViewModel> {

    private JCameraView jCameraView;

    public static final String PHOTO_PATH = "PHOTO_PATH";
    public static final String VIDEO_PATH = "VIDEO_PATH";

    public static final String RECORD_TYPE = "record_type";
    public static final String COME_FROM = "com_from";

    private int type = 0; //0 视频图片 1 图片 2视频
    private boolean isFromHome = false; // true 首页来 false 发布页面来


    @Override
    public int getLayoutRes() {
        return R.layout.hand_activity_publish_camera;
    }

    @Override
    public void initView(Bundle bundle) {
        jCameraView = mBinding.jcameraview;
        type = getIntent().getIntExtra(RECORD_TYPE, 0);
        isFromHome = getIntent().getBooleanExtra(COME_FROM, false);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + R.string.app_name);

        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        if (type == 0) {
            jCameraView.setTip("点击拍照，长按录像");
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        } else if (type == 1) {
            jCameraView.setTip("点击拍照");
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        } else if (type == 2) {
            jCameraView.setTip("长按录像");
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        }

        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(PublishCameraActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("BEIQI", bitmap);
                Intent intent = new Intent();
                intent.putExtra(PHOTO_PATH, path);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
//                String path = FileUtil.saveBitmap("BEIQI", firstFrame);
                Intent intent = new Intent();
                intent.putExtra(VIDEO_PATH, url);
                if (isFromHome) {
                    intent.setClass(PublishCameraActivity.this, PostActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                Log.i("CJT", "url = " + url + ", Bitmap = " + url);
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                PublishCameraActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(PublishCameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

}
