package com.handong.framework.api;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

public class ApiConfigration {


    String debugBaseUrl;
    String releaseBaseUrl;

    List<Interceptor> interceptors = new ArrayList<>();

    public ApiConfigration debugBaseUrl(String debugBaseUrl){
        this.debugBaseUrl = debugBaseUrl;
        return this;
    }

    public ApiConfigration releaseBaseUrl(String releaseBaseUrl){
        this.releaseBaseUrl = releaseBaseUrl;
        return this;
    }

    public ApiConfigration addInterceptor(Interceptor interceptor){
        if (!interceptors.contains(interceptor)) {
            interceptors.add(interceptor);
        }
        return this;
    }

}
