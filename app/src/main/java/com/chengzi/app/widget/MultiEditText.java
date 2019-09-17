package com.chengzi.app.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiEditText extends AppCompatEditText {

    public MultiEditText(Context context) {
        super(context);
        init();
    }

    public MultiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private List<String> atUserIds;

    public List<String> getAtUserIds() {
        return atUserIds;
    }

    private void init() {

        InputFilter[] filters = getFilters();
        InputFilter[] newFilters = new InputFilter[filters.length + 1];
        System.arraycopy(filters, 0, newFilters, 0, filters.length);
        newFilters[newFilters.length - 1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (TextUtils.equals(source, "@")) {
                    if (listener != null) {
                        listener.onRecive();
                    }
                }
                return null;
            }
        };
        setFilters(newFilters);
        setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    int selectionStart = getSelectionStart();
                    int selectionEnd = getSelectionEnd();

                    int newStart = selectionStart;
                    int newEnd = selectionEnd;

                    Editable text = getText();
                    ForegroundColorSpan[] spans = getSpans();
                    for (ForegroundColorSpan span : spans) {
                        int spanStart = getEditableText().getSpanStart(span);
                        int spanEnd = getEditableText().getSpanEnd(span);
                        if (selectionStart == selectionEnd && selectionEnd == spanEnd) {
                            newStart = spanStart;
                        }
                        if (selectionStart > spanStart && selectionStart < spanEnd) {
                            newStart = spanStart;
                        }
                        if (selectionEnd < spanEnd && selectionEnd > spanStart) {
                            newEnd = spanEnd;
                        }
                    }

                    if (newStart == selectionStart && newEnd == selectionEnd) {
                        return false;
                    }

                    setSelection(newStart, newEnd);
                    return true;
                }
            }

            return false;
        });
    }

    public String generateContent() {

        atUserIds = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();

        AtSpan[] spans = getSpans();
        if (spans.length == 0) {
            jsonArray.add(generateJsonObject(getText().toString(), null, 0, 0));
        } else {

            List<Position> positions = new ArrayList<>();
            for (AtSpan span : spans) {
                int spanStart = getEditableText().getSpanStart(span);
                int spanEnd = getEditableText().getSpanEnd(span);
                positions.add(new Position(spanStart,spanEnd,span));
            }

            Collections.sort(positions);

            Log.d("aaa", "generateContent: "+positions.toString());

            int previous = 0;

            for (Position position : positions) {
                int start = position.start;
                int end = position.end;
                AtSpan span = position.span;
                if (start > previous) {
                    String string = getText().subSequence(previous, start).toString().trim();
                    if (!TextUtils.isEmpty(string)) {
                        jsonArray.add(generateJsonObject(string, null, 0, 0));
                    }
                }
                atUserIds.add(span.getUserId());
                jsonArray.add(generateJsonObject(getText().subSequence(start, end).toString(), span.getUserId(), span.getUserType(), 1));
                previous = end;
            }

            if (previous < getText().length()) {
                String string = getText().subSequence(previous, getText().length()).toString().trim();
                if (!TextUtils.isEmpty(string)) {
                    jsonArray.add(generateJsonObject(string, null, 0, 0));
                }
            }

        }

        return jsonArray.toString();
    }

    private JsonObject generateJsonObject(String text, String userId, int userType, int type) {
        JsonObject object = new JsonObject();
        object.addProperty("text", text);
        if (!TextUtils.isEmpty(userId)) {
            object.addProperty("userId", userId);
        }
        object.addProperty("userType", userType);
        object.addProperty("type", type);
        return object;
    }

    public void appendAt(AtSpan atSpan) {
        if (TextUtils.isEmpty(atSpan.getUserName())) {
            throw new RuntimeException();
        }

        //  删除 @ 符号
        int selectionStart = getSelectionStart();
        if (selectionStart >= 1) {
            getEditableText().delete(selectionStart - 1, selectionStart);
        }

        selectionStart = getSelectionStart();

        SpannableString ss = new SpannableString(atSpan.getUserName());
        ss.setSpan(atSpan, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        getEditableText().insert(selectionStart, ss);
        getEditableText().insert(selectionStart + ss.length(), " ");

        ForegroundColorSpan[] spans = getSpans();

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();

        int newStart = selectionStart;
        int newEnd = selectionEnd;
        ForegroundColorSpan[] spans = getSpans();
        for (ForegroundColorSpan span : spans) {
            int spanStart = getEditableText().getSpanStart(span);
            int spanEnd = getEditableText().getSpanEnd(span);

            if (selectionStart > spanStart && selectionStart < spanEnd) {
                newStart = spanStart;
            }

            if (selectionEnd < spanEnd && selectionEnd > spanStart) {
                newEnd = spanEnd;
            }
        }
        if (newStart != selectionStart || newEnd != selectionEnd) {
            setSelection(newStart, newEnd);
        }

    }

    private AtSpan[] getSpans() {
        int len = getText().length();
        AtSpan[] spans = getEditableText().getSpans(0, len, AtSpan.class);
        for (AtSpan span : spans) {
            int spanStart = getEditableText().getSpanStart(span);
            int spanEnd = getEditableText().getSpanEnd(span);
            if (spanStart == spanEnd) {
                getEditableText().removeSpan(span);
            }
        }
        len = getText().length();
        spans = getEditableText().getSpans(0, len, AtSpan.class);
        return spans;
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onRecive();
    }

    private static class Position implements Comparable<Position> {
        int start;
        int end;
        AtSpan span;

        public Position(int start, int end, AtSpan span) {
            this.start = start;
            this.end = end;
            this.span = span;
        }

        @Override
        public int compareTo(Position o) {
            return start - o.start;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

}
