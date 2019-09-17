package com.chengzi.app.utils;

import com.blankj.utilcode.util.SPUtils;

public class PreferenceManager {

    private static final String PRE_TAG = "PreferenceManager";

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static void setPreference(String key, String value) {
        SPUtils.getInstance(PRE_TAG)
                .put(key, value);
    }

    public static String getPreference(String key) {
        return SPUtils.getInstance(PRE_TAG)
                .getString(key);
    }

}
