package com.handong.framework.base;

import android.annotation.SuppressLint;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.stream.MalformedJsonException;
import com.handong.framework.api.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;

@SuppressLint("RestrictedApi")
public class BaseViewModel extends ViewModel implements GenericLifecycleObserver {


    public final MutableLiveData<String> tokenExpir = new MutableLiveData<>();
    public final MutableLiveData<String> multipleDevice = new MutableLiveData<>();
    public final MutableLiveData<String> accountFrozen = new MutableLiveData<>();
    public final MutableLiveData<String> otherFrozen = new MutableLiveData<>();
    public final MutableLiveData<Boolean> error = new MutableLiveData<>();

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        switch (event) {
            case ON_DESTROY:
                compositeDisposable.dispose();
                break;
        }
    }

    protected abstract class SimpleObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(T t) {
            if (t instanceof ResponseBean) {

                ResponseBean responseBean = (ResponseBean) t;
                String msg = responseBean.getMessage();
                int status = responseBean.getStatus();

                if (responseBean.isSuccess()) {

                    onSuccess(t);

                } else if (responseBean.isMultipeDevice()) {
                    multipleDevice.postValue("您的账号已在其他设备上登录");
                } else if (responseBean.isAccountFrozen()) {
                    accountFrozen.postValue("您的账号已被删除或冻结");
                } else if (responseBean.isTokenExpire()) {
                    tokenExpir.postValue("token已过期，请重新登录");
                } else if (responseBean.isOtherForzen()) {
                    otherFrozen.postValue(TextUtils.isEmpty(msg) ? "该账号已被删除或冻结" : msg);
                } else {
                    onError(status, msg);
                }
            } else {
                onSuccess(t);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof CompositeException) {
                CompositeException compositeE = (CompositeException) e;
                for (Throwable throwable : compositeE.getExceptions()) {
                    if (throwable instanceof SocketTimeoutException) {
                        onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
                    } else if (throwable instanceof ConnectException) {
                        onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                    } else if (throwable instanceof UnknownHostException) {
                        onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                    } else if (throwable instanceof MalformedJsonException) {
                        onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
                    } else if (e instanceof RuntimeException) {
                        onError(ApiException.Code_Runtime, e.getMessage());
                    } else {
                        onError(ApiException.Code_Default, throwable.getMessage());
                    }
                }
            } else if (e instanceof SocketTimeoutException) {
                onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
            } else if (e instanceof ConnectException) {
                onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
            } else if (e instanceof UnknownHostException) {
                onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
            } else if (e instanceof MalformedJsonException) {
                onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
            } else if (e instanceof RuntimeException) {
                onError(ApiException.Code_Runtime, e.getMessage());
            } else {

                String msg = e.getMessage();
                if (msg == null) {
                    onError(ApiException.Code_Default, e.toString());
                    return;
                }

                if (msg.toLowerCase().contains("token")) {
                    tokenExpir.postValue("token已过期，请重新登录");
                } else {
                    onError(ApiException.Code_Default, e == null ? "unknow error" : e.getMessage());
                }

            }


        }

        @Override
        public void onComplete() {

        }

        /**
         * 加载成功
         *
         * @param t 获得的数据实体
         */
        public abstract void onSuccess(@NonNull T t);


        /**
         * 连接失败或返回数据错误
         *
         * @param code errorMsg
         */
        public void onError(int code, String errorMsg) {
            error.postValue(true);
            if (!TextUtils.isEmpty(errorMsg)) {
                ToastUtils.showShort(errorMsg);
            }
        }
    }
}
