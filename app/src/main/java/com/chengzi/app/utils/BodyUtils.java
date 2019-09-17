package com.chengzi.app.utils;

import java.io.File;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BodyUtils {

    public static MultipartBody.Part createMultiPart(String filePath){
        File file = new File(filePath);
        return createMultiPart(file);
    }

    public static MultipartBody.Part createMultiPart(File file){
        String contentType = HttpURLConnection.guessContentTypeFromName(file.getName());
        RequestBody body = RequestBody.create(MediaType.parse(contentType), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), body);
        return fileBody;
    }
}
