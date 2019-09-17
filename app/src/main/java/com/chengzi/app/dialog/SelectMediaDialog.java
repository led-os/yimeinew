package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandDialogPublishLayoutBinding;

public class SelectMediaDialog extends BottomDialog implements View.OnClickListener {

    private int options;
    private boolean isShowCamera = true;

    public void setOptions(int options) {
        this.options = options;
    }

    public void setVisibilityBtnCamera(boolean isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    private HandDialogPublishLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.hand_dialog_publish_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setListener(this);
        if (options == PICTURE) {
//            binding.btnCamera.setVisibility(View.GONE);
            binding.btnVideo.setVisibility(View.GONE);
        } else if (options > 0) {
            binding.btnPic.setVisibility(View.GONE);
        }
        binding.btnCamera.setVisibility(isShowCamera ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_camera) {
            if (listener != null) {
                listener.onOptionClick(CAMERA);
            }
        } else if (id == R.id.btn_pic) {
            if (listener != null) {
                listener.onOptionClick(PICTURE);
            }
        } else if (id == R.id.btn_video) {
            if (listener != null) {
                listener.onOptionClick(VIDEO);
            }
        }
        dismissAllowingStateLoss();
    }

    private OnOptionClickListener listener;

    public void setListener(OnOptionClickListener listener) {
        this.listener = listener;
    }

    public interface OnOptionClickListener {
        void onOptionClick(@OptionsRef int options);
    }

    @IntDef(value = {CAMERA, PICTURE, VIDEO})
    public @interface OptionsRef {
    }

    public static final int CAMERA = 0x10;
    public static final int PICTURE = 0x20;
    public static final int VIDEO = 0x30;
}
