package com.chengzi.app.utils;

import java.util.ArrayList;
import java.util.List;

//获取list中已上传/未上传的图片数据
public class ImageIsUploadUtils {

    //已上传的图片地址,不需要再次上传
    public static List<String> isUploadImage(List<String> filePaths) {
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            String filePath = filePaths.get(i);
            //已上传过的图片
            if (filePath.contains("http://") || filePath.contains("https://")) {
                imgs.add(filePath);
            }
        }
        return imgs;
    }

    //要上传的图片文件
    public static List<String> isNotUploadImage(List<String> filePaths) {
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            String filePath = filePaths.get(i);
            //需上传的图片
            if (!filePath.contains("http://") && !filePath.contains("https://")) {
                imgs.add(filePath);
            }
        }
        return imgs;
    }
}
