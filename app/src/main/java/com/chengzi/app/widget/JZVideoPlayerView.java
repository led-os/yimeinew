package com.chengzi.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chengzi.app.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @ClassName:JZVideoPlayerView
 * @PackageName:com.hangdongkeji.arcfox.widget
 * @Create On 2018/12/10 0010   10:45
 * @Site:http://www.handongkeji.com
 * @author:xinghuaiwang
 * @Copyrights 2018/12/10 0010 handongkeji All rights reserved.
 */

public class JZVideoPlayerView extends JzvdStd {

    private int width;
    private boolean isPlaying = false;
    private boolean isFirst = false;
    public JZVideoPlayerView(Context context) {
        super(context, null);
    }

    public JZVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadingProgressBar.setVisibility(View.GONE);
        width = context.getResources().getDisplayMetrics().widthPixels;
        setAllControlsVisiblity(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
        hideView();
    }

    private void hideView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            ViewGroup childAt = (ViewGroup) getChildAt(0);
            int childCount1 = childAt.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View child = childAt.getChildAt(i);
                if (child != textureViewContainer) {
                    ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                    if (layoutParams == null) {
                        layoutParams = new ViewGroup.LayoutParams(0, 0);
                    } else {
                        layoutParams.width = 0;
                        layoutParams.height = 0;
                    }
                    child.setLayoutParams(layoutParams);
                }
            }

        }

    }

    public void setUrl(String url) {
        setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
    }

    public void startPlay() {
        startVideo();
        setAllControlsVisiblity(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        startVideo();
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        setAllControlsVisiblity(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
        return true;
    }

    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        if (progress != 0) {
            bottomProgressBar.setProgress(progress);
        }
        bottomProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void dissmissControlView() {
        if (currentState != CURRENT_STATE_NORMAL && currentState != CURRENT_STATE_ERROR && currentState != CURRENT_STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (currentScreen != SCREEN_WINDOW_TINY) {
                    bottomProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setVisibility(GONE);
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
            replayTextView.setVisibility(GONE);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setVisibility(GONE);
            replayTextView.setVisibility(GONE);
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            startButton.setVisibility(GONE);
            startButton.setImageResource(R.drawable.jz_click_replay_selector);
            replayTextView.setVisibility(GONE);
        } else {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
            replayTextView.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        isFirst = true;
        isPlaying = true;
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isFirst() {
        return isFirst;
    }
}
