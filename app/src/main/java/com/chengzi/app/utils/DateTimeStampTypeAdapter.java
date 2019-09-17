package com.chengzi.app.utils;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeStampTypeAdapter extends TypeAdapter<Long> {

    @Override
    public void write(JsonWriter out, Long value) throws IOException {
        if (value == null) {
            out.nullValue();
        }else{
            out.value(value);
        }
    }

    @Override
    public Long read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NULL:
                in.nextNull();
                return 0L;
            case NUMBER:
                long l = in.nextLong();
                return l;
            case STRING:
                String value = in.nextString();
                if (TextUtils.isEmpty(value)) {
                    return 0L;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parse = sdf.parse(value);
                    return parse.getTime()/1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0L;
        }
        return 0L;
    }
}
