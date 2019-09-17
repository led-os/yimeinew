package com.chengzi.app.utils;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FloatTypeAdapter extends TypeAdapter<Float> {
    @Override
    public void write(JsonWriter out, Float value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Float read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NULL:
                in.nextNull();
                return null;
            case NUMBER:
                return (float) in.nextDouble();
            case STRING:
                String str = in.nextString();
                if (TextUtils.isEmpty(str)) {
                    return 0f;
                }
                try {
                    float value = Float.parseFloat(str);
                    return value;
                }catch (Exception e){
                    return 0f;
                }
        }
        return null;
    }
}
