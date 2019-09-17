package com.handong.framework.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.handong.framework.base.PageBean;

import java.io.IOException;

public class BasePageBeanTypeAdapter<T, K> extends TypeAdapter<PageBean<T>> {

    private Gson gson;
    private BaseBeanTypeAdapterFactory factory;
    private TypeToken<K> type;
    private TypeAdapter<PageBean<T>> delegateAdapter;

    public BasePageBeanTypeAdapter(Gson gson, BaseBeanTypeAdapterFactory factory, TypeToken<K> type) {
        this.gson = gson;
        this.factory = factory;
        this.type = type;
        delegateAdapter = (TypeAdapter<PageBean<T>>) gson.getDelegateAdapter(factory, type);
    }

    @Override
    public void write(JsonWriter out, PageBean value) throws IOException {
        delegateAdapter.write(out, value);
    }

    @Override
    public PageBean read(JsonReader in) throws IOException {
        PageBean<T> pageBean = delegateAdapter.read(in);
        if (pageBean == null) {
            pageBean = new PageBean<>();
        }
        return pageBean;
    }
}
