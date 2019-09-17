package com.chengzi.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chengzi.app.R;

/**
 * @ClassName:SelectDialog
 * @PackageName:com.yimei.app.dialog
 * @Create On 2019/3/26 0026   19:00
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */

public class PayDialog extends Dialog {
    private TextView tv_pay_wx, tv_pay_ali, tv_cancel;
    private View.OnClickListener listenerPhoto, listenerCamera, listenerCancel;

    public PayDialog(Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        setContentView(R.layout.dialog_pay);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setGravity(Gravity.BOTTOM);

        tv_pay_wx = findViewById(R.id.tv_pay_wx);
        tv_pay_ali = findViewById(R.id.tv_pay_ali);
        tv_cancel = findViewById(R.id.tv_cancel);
        //微信
        tv_pay_wx.setOnClickListener(v -> {
            dismiss();
            if (listenerPhoto != null) {
                listenerPhoto.onClick(tv_pay_wx);
            }
        });
        //支付宝
        tv_pay_ali.setOnClickListener(v -> {
            dismiss();
            if (listenerCamera != null) {
                listenerCamera.onClick(tv_pay_ali);
            }
        });
        //取消
        tv_cancel.setOnClickListener(v -> {
            dismiss();
            if (listenerCancel != null) {
                listenerCancel.onClick(tv_cancel);
            }
        });
    }

    /**
     * 设置对话框的消息文本
     */
    public PayDialog setPayWX(String text1) {
        tv_pay_wx.setText(text1);
        return this;
    }

    public PayDialog setPayAli(String text2) {
        tv_pay_ali.setText(text2);
        return this;
    }

    public PayDialog setTvCancle(String cancle) {
        tv_cancel.setText(cancle);
        return this;
    }

    public PayDialog setTvCancleVisibility(boolean isShow) {
        tv_cancel.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public PayDialog setPayWXListener(View.OnClickListener listener) {
        listenerPhoto = listener;
        return this;
    }

    public PayDialog setPayAliListener(View.OnClickListener listener) {
        this.listenerCamera = listener;
        return this;
    }

    public PayDialog setText0Listener(View.OnClickListener listener) {
        this.listenerCancel = listener;
        return this;
    }
}