package com.chengzi.app.utils;

import com.blankj.utilcode.util.EncryptUtils;
import com.handong.framework.account.AccountHelper;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ParamsEncryptInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origin = chain.request();
        String method = origin.method();
        if (method.equalsIgnoreCase("get")) {
            return chain.proceed(processGet(origin));
        } else if (method.equalsIgnoreCase("post")) {
            return chain.proceed(processPost(origin));
        }

        Response response = chain.proceed(origin);

        return response;
    }

    private Request processGet(Request origin) {
        HttpUrl url = origin.url();

        String timestampName = "timestamp";
        String timestampValue = String.valueOf(System.currentTimeMillis() / 1000);

        String cityIdKey = "city_id";
        String cityIdValue = PreferenceManager.getPreference(PreferenceManager.CITY_ID);

        String tokenKey = "token";
        String tokenValue = AccountHelper.getToken();

        List<String> names = new ArrayList<>();
        Map<String, List<String>> nameAndValues = new HashMap<>();

        int querySize = url.querySize();
        for (int i = 0; i < querySize; i++) {

            String name = URLDecoder.decode(url.queryParameterName(i));
            String value = URLDecoder.decode(url.queryParameterValue(i));

            if (!names.contains(name)) {
                names.add(name);
            }
            List<String> list = nameAndValues.get(name);
            if (list == null) {
                nameAndValues.put(name, list = new ArrayList<>());
            }
            list.add(value);
        }

        if (!names.contains(cityIdKey)) {
            names.add(cityIdKey);
            List<String> list = new ArrayList<>();
            list.add(cityIdValue);
            nameAndValues.put(cityIdKey, list);
        }

        if (!names.contains(tokenKey)) {
            names.add(tokenKey);
            List<String> list = new ArrayList<>();
            list.add(tokenValue);
            nameAndValues.put(tokenKey, list);
        }

        names.add(timestampName);
        List<String> timestampList = new ArrayList<>();
        timestampList.add(timestampValue);
        nameAndValues.put(timestampName, timestampList);

        Collections.sort(names);

        StringBuffer sb = new StringBuffer();

        for (String name : names) {
            List<String> list = nameAndValues.get(name);
            for (String value : list) {
                sb.append(value)
                        .append(",");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        String sign = EncryptUtils.encryptMD5ToString(sb.toString()).toLowerCase();
        HttpUrl.Builder builder = url.newBuilder();
        for (String name : names) {
            List<String> list = nameAndValues.get(name);
            builder.removeAllQueryParameters(name);
            for (String value : list) {
                builder.addQueryParameter(name, value);
            }
        }
        builder.addQueryParameter("sign", sign);

        HttpUrl httpUrl = builder.build();

        Request request = origin.newBuilder().url(httpUrl).get().build();
        return request;
    }

    private Request processPost(Request origin) {

        RequestBody body = origin.body();
        if (!FormBody.class.isInstance(body)) {
            return origin;
        }

        String timestampName = "timestamp";
        String timestampValue = String.valueOf(System.currentTimeMillis() / 1000);

        String cityIdKey = "city_id";
        String cityIdValue = PreferenceManager.getPreference(PreferenceManager.CITY_ID);

        String tokenKey = "token";
        String tokenValue = AccountHelper.getToken();

        List<String> names = new ArrayList<>();
        Map<String, List<String>> nameAndValues = new HashMap<>();

        FormBody formBody = (FormBody) body;
        for (int i = 0; i < formBody.size(); i++) {

            String name = URLDecoder.decode(formBody.encodedName(i));
            String value = URLDecoder.decode(formBody.encodedValue(i));

            List<String> list = nameAndValues.get(name);
            if (list == null) {
                nameAndValues.put(name, list = new ArrayList<>());
            }
            list.add(value);
            if (!names.contains(name)) {
                names.add(name);
            }
        }

        if (!names.contains(cityIdKey)) {
            names.add(cityIdKey);
            List<String> list = new ArrayList<>();
            list.add(cityIdValue);
            nameAndValues.put(cityIdKey, list);
        }

        if (!names.contains(tokenKey)) {
            names.add(tokenKey);
            List<String> list = new ArrayList<>();
            list.add(tokenValue);
            nameAndValues.put(tokenKey, list);
        }

        List<String> timestampList = new ArrayList<>();
        timestampList.add(timestampValue);
        nameAndValues.put(timestampName, timestampList);
        names.add(timestampName);

        Collections.sort(names);

        StringBuffer sb = new StringBuffer();
        for (String name : names) {
            List<String> list = nameAndValues.get(name);
            for (String value : list) {
                sb.append(value)
                        .append(",");
            }

        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String sign = EncryptUtils.encryptMD5ToString(sb.toString()).toLowerCase();

        FormBody.Builder builder = new FormBody.Builder();

        for (String name : names) {
            List<String> list = nameAndValues.get(name);
            for (String value : list) {
                builder.add(name, value);
            }
        }

        builder.add("sign", sign);

        Request request = origin.newBuilder().post(builder.build()).build();
        return request;
    }
}
