package com.chengzi.app.third.fileuplod;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.handong.framework.account.AccountHelper;
import com.chengzi.app.MyApp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Desc:
 * @ClassName:AliUpload
 * @PackageName:com.yimei.app.third.fileuplod
 * @Create On 2019/4/28 0028
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class AliUpload {

    /**
     * 阿里云OSS对象存储 -单文件上传
     *
     * @param
     * @param objectKey
     * @param path
     * @return
     */
    public static void upload(String objectKey, String path, CallBack callBack) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 构造上传请求
            PutObjectRequest request =
                    new PutObjectRequest(AliConfig.OSS_BUCKET_NAME,
                            objectKey, path);
            request.setCRC64(OSSRequest.CRC64Config.YES);
            try {
                //得到client
                OSS client = getOSSClient();
                //上传获取结果
                PutObjectResult result = client.putObject(request);
                Log.e("message", "upload: 上传获取结果" + result);
                //获取可访问的url
                String url = client.presignPublicObjectURL(AliConfig.OSS_BUCKET_NAME, objectKey);
                //格式打印输出
                Log.e("message", "upload: " + url);
                emitter.onNext(url);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String url) {
                if (callBack != null) {
                    callBack.onSuccess(url);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (callBack != null) {
                    callBack.onError(new AliError("500", e.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 多图片上传
     */
    public static void multipleUpload( List<String> paths, MultipleCallBack multipleCallBack) {
        List<Observable<String>> observableList = new ArrayList<>(paths.size());
        List<String> urls = new ArrayList<>(paths.size());
        List<AliError> aliErrors = new ArrayList<>();
        for (String path : paths
                ) {
            Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    //每次计算->key 否则多个图片同时上传 有问题
                    String objectKey = System.currentTimeMillis() + AccountHelper.getUserId();
                    // 构造上传请求
                    PutObjectRequest request =
                            new PutObjectRequest(AliConfig.OSS_BUCKET_NAME, objectKey
                                    , path);
                    request.setCRC64(OSSRequest.CRC64Config.YES);
                    try {
                        //得到client
                        OSS client = getOSSClient();
                        //上传获取结果
                        PutObjectResult result = client.putObject(request);
                        Log.e("message", "upload: 上传获取结果" + result);
                        //获取可访问的url
                        String url = client.presignPublicObjectURL(AliConfig.OSS_BUCKET_NAME, objectKey);
                        //格式打印输出
                        Log.e("message", "upload: " + url);
                        emitter.onNext(url);
                        emitter.onComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }
            });
            observableList.add(observable);
        }
        Observable.concat(observableList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String url) {
                urls.add(url);
            }

            @Override
            public void onError(Throwable e) {
                aliErrors.add(new AliError("500", e.getMessage()));
            }

            @Override
            public void onComplete() {
                if (multipleCallBack != null) {
                    multipleCallBack.onSuccess(urls);
                } else {
                    multipleCallBack.onError(aliErrors);
                }
            }
        });
    }

    private static OSS getOSSClient() {
        // 配置类如果不设置，会有默认配置。
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider(AliConfig.ACCESS_KEY_ID,
                        AliConfig.ACCESS_KEY_SECRET);
        return new OSSClient(MyApp.getInstance(), AliConfig.OSS_ENDPOINT, credentialProvider);
    }

    public interface CallBack {
        void onSuccess(String path);

        void onError(AliError aliError);
    }

    public interface MultipleCallBack {
        ArrayList<String> onSuccess(List<String> paths);

        ArrayList<String> onError(List<AliError> errors);
    }
}
