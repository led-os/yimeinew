package com.chengzi.app.utils;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntTypeAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        if (value == null) {
            out.nullValue();
        }else{
            out.value(value);
        }
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NUMBER:
                return in.nextInt();
            case BOOLEAN:
                return in.nextBoolean()?1:0;
            case STRING:
                return toInteger(in.nextString());
            case NULL:
                in.nextNull();
                return null;
        }
        return null;
    }

    /**
     * true  TURE 都为true
     * "0" 为 false
     * "1" 为 true
     * @param name
     * @return
     */
    public static int toInteger(String name) {
        if (TextUtils.isEmpty(name)){
            return 0;
        }else{
            if (name.equalsIgnoreCase("true")){
                return 1;
            }else if (name.equalsIgnoreCase("false")){
                return 0;
            }else if (name.equals("1")){
                return 1;
            }else if (name.equals("0")){
                return 0;
            }
        }
        return 0;
    }

}
