package com.chengzi.app.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;

public class ProxyHelper {

    private static HttpProxyCacheServer proxy;

    public static void initProxy(Application application){
        HttpProxyCacheServer proxy = new HttpProxyCacheServer.Builder(application)
                .maxCacheFilesCount(100)
                .maxCacheSize(1024 * 1024 * 100)
                .cacheDirectory(cacheDir(application))
                .build();
        ProxyHelper.proxy = proxy;
    }

    private static File cacheDir(Context context) {
        File parentDir = null;
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            parentDir = context.getExternalCacheDir();
        } else {
            parentDir = context.getCacheDir();
        }
        File cacheDir = new File(parentDir, "videoCache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static HttpProxyCacheServer getProxy(){
        return proxy;
    }

}
