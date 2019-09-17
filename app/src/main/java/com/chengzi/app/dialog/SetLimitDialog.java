package com.chengzi.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.chengzi.app.R;


/**
 * Created by Administrator on 2018/10/12 0012.
 * com.wanjing.app.dialog
 */

public class SetLimitDialog extends Dialog {
    private TextView tvTitle;
    private EditText tvMessage;
    private TextView btnCancle, tvSure;
    private TextView tv_yuan;
    private View view;

    public SetLimitDialog(Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        SetLimitDialogs();
    }

    public SetLimitDialog(Context context, int limit) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        SetLimitDialogs();
        setPriceMode(tvMessage, limit);

    }

    public void SetLimitDialogs() {
        this.setContentView(R.layout.dialog_set_limit);
        tvTitle = findViewById(R.id.tv_title);
        tvMessage = findViewById(R.id.tv_message);
        tv_yuan = findViewById(R.id.tv_yuan);
        btnCancle = findViewById(R.id.tv_cancle);
        tvSure = findViewById(R.id.tv_sure);
        view = findViewById(R.id.view);
        tvMessage.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnCancle.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onClick(tvSure);
            }
        });
        tvSure.setOnClickListener(v -> {
            dismiss();
            if (listener1 != null) {
                listener1.onClick(tvMessage.getText().toString());
            }
        });
    }

    public SetLimitDialog setBtnCancleHint(boolean isHineCancle) {
        if (isHineCancle) {
            btnCancle.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置对话框的消息文本
     */
    public SetLimitDialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public SetLimitDialog setMessageHint(String messageHint) {
        tvMessage.setHint(messageHint);
        return this;
    }

    public SetLimitDialog setYuan(boolean isshow) {
        tv_yuan.setVisibility(isshow ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置EditText为价钱输入模式
     *
     * @param editText 相应的EditText
     * @param digits   限制的小数位数
     */
    public static void setPriceMode(final EditText editText, final int digits) {
//        设置输入类型为小数数字，允许十进制小数点提供分数值。
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        给EditText设置文本变动监听事件
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                如果文本包括"."，删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + digits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length()); //光标移到最后
                    }
                }
//                未输入数字的情况下输入小数点时，个位数字自动补零
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
//                如果文本以"0"开头并且第二个字符不是"."，不允许继续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    public SetLimitDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public SetLimitDialog setTvSure(String sure) {
        tvSure.setText(sure);
        return this;
    }

    public SetLimitDialog setTvCancle(String cancle) {
        btnCancle.setText(cancle);
        return this;
    }

    private OnClickSureListener listener1;

    public SetLimitDialog setSureListener(OnClickSureListener listener) {
        listener1 = listener;
        return this;
    }

    private View.OnClickListener listener;

    public SetLimitDialog setCancleListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnClickSureListener {
        void onClick(String text);
    }
}