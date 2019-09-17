package com.chengzi.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.handong.framework.base.BaseActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.dialog.SelectMediaDialog;
import com.chengzi.app.ui.discover.activity.PublishCameraActivity;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.RECORD_TYPE;

public class CertifiedHelp {

    /**
     * 选择/拍照 1张
     *
     * @param context
     * @param isShowCamera 是否显示拍照
     */
    public static void showDialog(Activity context, boolean isShowCamera) {
        showDialog1(context, isShowCamera, 1);
    }

    public static void showDialog(Activity context, boolean isShowCamera, String text) {

        showDialog1(context, isShowCamera, 1);
    }

    public static void showDialogWithCrop(Activity context, boolean isShowCamera) {
        showDialog1(context, isShowCamera, 1, true);
    }

    /**
     * @param context
     * @param isShowCamera
     * @param num          最多9张-->num=9-已有num
     */
    public static void showNineDialog(Activity context, boolean isShowCamera, int num) {
        showDialog1(context, isShowCamera, num);
    }

    public static void showDialog1(Activity context, boolean isShowCamera, int num) {
        showDialog1(context, isShowCamera, num, false);
    }

    public static void showDialog1(Activity context, boolean isShowCamera, int num, boolean crop) {
        SelectMediaDialog optionsDialog = new SelectMediaDialog();
        optionsDialog.setOptions(SelectMediaDialog.PICTURE);
        optionsDialog.show(((FragmentActivity) context).getSupportFragmentManager(), SelectMediaDialog.class.getSimpleName());
        optionsDialog.setVisibilityBtnCamera(isShowCamera);
        optionsDialog.setListener(options -> {
            switch (options) {
                case SelectMediaDialog.CAMERA:
                    List<String> permissions = new ArrayList<>();
                    if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.RECORD_AUDIO)) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
                        permissions.add(Manifest.permission.CAMERA);
                    }
                    PermissionUtils.permission(permissions.toArray(new String[permissions.size()]))
                            .callback(new PermissionUtils.SimpleCallback() {
                                @Override
                                public void onGranted() {
                                    openCamera(context);
                                }

                                @Override
                                public void onDenied() {

                                }
                            }).request();
                    break;
                case SelectMediaDialog.PICTURE:
                    if (crop) {
                        selectPictureWithCrop(context);
                    } else {
                        openPicture(context, num);
                    }
                    break;
            }
        });
    }


    public static void openCamera(Context context) {
        Intent intent = new Intent(context, PublishCameraActivity.class);
        intent.putExtra(RECORD_TYPE, 1);
        ((BaseActivity) context).startActivityForResult(intent, Sys.REQUEST_CODE_CAMERA);
    }

    public static void openPicture(Context context, int needPicNum) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(true);
        imagePicker.setShowCamera(true);
        if (needPicNum < 0 || needPicNum > 9) {
            needPicNum = 1;
        }
        imagePicker.setSelectLimit(needPicNum);
        Intent intent = new Intent(context, ImageGridActivity.class);
        ((BaseActivity) context).startActivityForResult(intent, Sys.REQUEST_CODE_PIC);
    }

    public static void selectPictureWithCrop(Context context) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(true);
        imagePicker.setShowCamera(true);
        imagePicker.setMultiMode(false);

        //  1190x380 推广banner 图片尺寸
        int width = ScreenUtils.getScreenWidth();
        int height = width * 380 / 1190;
        imagePicker.setFocusHeight(height);
        imagePicker.setFocusWidth(width);
        imagePicker.setOutPutX(width);
        imagePicker.setOutPutY(height);
        Intent intent = new Intent(context, ImageGridActivity.class);
        ((BaseActivity) context).startActivityForResult(intent, Sys.REQUEST_CODE_PIC);
    }
}
