package com.chengzi.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;

import static com.chengzi.app.utils.CharacterUtils.isChinese;
import static com.chengzi.app.utils.CharacterUtils.isEn;

public class CommentEditText extends AppCompatEditText {

    private String reply;
    protected boolean disableEmoji;
    protected int maxLength;

    public CommentEditText(Context context) {
        super(context);
        init(context, null);
    }

    public CommentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public String getReply() {
        return reply == null ? "" : reply;
    }

    public void setReply(String reply) {
        if (TextUtils.isEmpty(reply)) {

            setText("");

        } else {
            this.reply = "回复" + reply;
            SpannableString ss = new SpannableString(this.reply);
            ForegroundColorSpan fcs = new ForegroundColorSpan(0xFF646B84);
            ss.setSpan(fcs, 2, this.reply.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(ss);
        }

    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.hand_custom_edittext);

        //  禁用软键盘表情  ，false 不禁用，true禁用
        disableEmoji = typedArray.getBoolean(R.styleable.hand_custom_edittext_hand_disableEmoji,false);
        //  可输入的最大字数
        maxLength = typedArray.getInt(R.styleable.hand_custom_edittext_hand_max_length,Integer.MAX_VALUE);
        typedArray.recycle();

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                    if (!TextUtils.isEmpty(reply)) {
                        int indexOf = getText().toString().indexOf(reply);
                        if (indexOf >= 0 && getText().toString().contains(reply)) {
                            if (getSelectionStart() == getSelectionEnd() && getSelectionStart() == indexOf + reply.length()) {
                                return true;
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(reply) && TextUtils.equals(getText(), reply)) {
                        return true;
                    }
                }
                return false;
            }
        });

        InputFilter[] filters = getFilters();
        InputFilter[] inputFilters = new InputFilter[filters.length + 1];
        System.arraycopy(filters, 0, inputFilters, 1, filters.length);
        inputFilters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (disableEmoji && !checkIsValid(source.toString())) {
                    return "";
                }
                Editable text = getText();
                if (!TextUtils.isEmpty(text) && (text.length()+source.length()) > maxLength) {
                    ToastUtils.showShort("最多输入"+maxLength+"字");
                    return "";
                }
                return null;
            }
        };
        setFilters(inputFilters);

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (!TextUtils.isEmpty(reply)) {
            String s = getText().toString();
            int indexOf = s.indexOf(reply);
            if (indexOf >= 0) {
                if (selStart >= indexOf && selStart <= indexOf + reply.length() - 1) {
                    setSelection(indexOf + reply.length(), selEnd);
                }

                if (selEnd >= indexOf && selEnd <= indexOf + reply.length() - 1) {
                    setSelection(indexOf + reply.length(), indexOf + reply.length());
                }
            }

        }
    }

    private boolean checkIsValid(String source) {
        boolean valid = true;
        int length = source.length();
        for (int i = 0; i < length; i++) {
            char c = source.charAt(i);
            if (!isChinese(c) && !isEn(c)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

}
