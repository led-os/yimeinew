package com.chengzi.app.utils;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DoubleTypeAdapter extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NULL:
                in.nextNull();
                return null;
            case STRING:
                String str = in.nextString();
                if (TextUtils.isEmpty(str)) {
                    return 0D;
                }
                try {
                    double value = Double.parseDouble(str);
                    return value;
                } catch (Exception e) {
                    return 0D;
                }
            case NUMBER:
                return in.nextDouble();
        }
        return 0D;
    }
}
