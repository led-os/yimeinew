package com.chengzi.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admininstrator on 2017/8/21 0021.
 */

public class ImagePickerUtil extends Fragment {

    private static SparseArray<OnImageCallback> sparseArray = new SparseArray<>();

    public interface OnImageCallback {
        void callBack(List<ImageItem> list);
    }

    private OnImageCallback callback;

    /**
     * 选择图片回调
     *
     * @param activity
     * @param requesCode
     * @param callback
     */
    public static void selectPic(Activity activity, int requesCode, OnImageCallback callback) {
        if (callback != null) {
            sparseArray.put(requesCode, callback);
        }
        Intent intent = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent, requesCode);
    }

    public static void pickSingleImage(Activity activity, int requestCode, ImagePickerUtil.OnImageCallback callback){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(false);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(1);
        selectPic(activity, requestCode, callback);
    }

    /**
     * 多选图片
     *
     * @param requestCode
     * @param imageCount
     * @param callback
     */
    public static void pickMultiImage(Activity activity, int requestCode, int imageCount, ImagePickerUtil.OnImageCallback callback) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(false);
        imagePicker.setShowCamera(false);
        if (imageCount < 0 || imageCount > 9) {
            imageCount = 1;
        }
        imagePicker.setSelectLimit(imageCount);
        selectPic(activity, requestCode, callback);
    }


    public static void takePhoto(Activity activity, int requestCode, OnImageCallback callback) {
        if (callback != null) {
            sparseArray.put(requestCode, callback);
        }
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 处理结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void handResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            OnImageCallback onImageCallback = sparseArray.get(requestCode);
            if (onImageCallback != null) {
                List<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                onImageCallback.callBack(images);
                sparseArray.remove(requestCode);
            }
        }
    }

    //创建获取 文件夹路径
    public static String isExistDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
}
