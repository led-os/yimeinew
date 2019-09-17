package com.chengzi.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.chengzi.app.R;


/**
 * Created by Administrator on 2018/10/12 0012.
 * com.wanjing.app.dialog
 */

public class BindAliDialog extends Dialog {
    private TextView tvTitle;
    private EditText tvMessage;
    private TextView btnCancle, tvSure;
    private TextView tv_yuan;
    private View view;

    public BindAliDialog(Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        this.setContentView(R.layout.dialog_set_limit);
        tvTitle = findViewById(R.id.tv_title);
        tvMessage = findViewById(R.id.tv_message);
        tv_yuan = findViewById(R.id.tv_yuan);
        btnCancle = findViewById(R.id.tv_cancle);
        tvSure = findViewById(R.id.tv_sure);
        view = findViewById(R.id.view);

        btnCancle.setOnClickListener(v ->
                dismiss()
        );
        tvSure.setOnClickListener(v -> {
            dismiss();
            if (listener1 != null) {
                listener1.onClick(tvMessage.getText().toString());
            }
        });
    }

    /**
     * 设置对话框的消息文本
     */
    public BindAliDialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public BindAliDialog setMessageHint(String messageHint) {
        tvMessage.setHint(messageHint);
        return this;
    }

    public BindAliDialog setYuan(boolean isshow) {
        tv_yuan.setVisibility(isshow ? View.VISIBLE : View.GONE);
        return this;
    }

    public BindAliDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public BindAliDialog setTvSure(String sure) {
        tvSure.setText(sure);
        return this;
    }

    private OnClickSureListener listener1;

    public BindAliDialog setSureListener(OnClickSureListener listener) {
        listener1 = listener;
        return this;
    }

    public interface OnClickSureListener {
        void onClick(String text);
    }
}