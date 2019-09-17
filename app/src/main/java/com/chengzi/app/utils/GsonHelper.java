package com.chengzi.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {


    private static final Gson GSON = new GsonBuilder().create();

    public static Gson getDefault(){
        return GSON;
    }

}
