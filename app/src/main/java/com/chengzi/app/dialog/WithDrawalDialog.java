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

public class WithDrawalDialog extends Dialog {
    private TextView tv_photo, tv_camera, tv_cancel;
    private View.OnClickListener listenerPhoto, listenerCamera, listenerCancel;

    public WithDrawalDialog(Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        setContentView(R.layout.dialog_with_drawal);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setGravity(Gravity.BOTTOM);

        tv_photo = findViewById(R.id.tv_photo);
        tv_camera = findViewById(R.id.tv_camera);
        tv_cancel = findViewById(R.id.tv_cancel);
        //银行卡
        tv_photo.setOnClickListener(v -> {
            dismiss();
            if (listenerPhoto != null) {
                listenerPhoto.onClick(tv_photo);
            }
        });
        //线下交接
        tv_camera.setOnClickListener(v -> {
            dismiss();
            if (listenerCamera != null) {
                listenerCamera.onClick(tv_camera);
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
    public WithDrawalDialog setTvText1(String text1) {
        tv_photo.setText(text1);
        return this;
    }

    public WithDrawalDialog setTvText2(String text2) {
        tv_camera.setText(text2);
        return this;
    }

    public WithDrawalDialog setTvText1Visible(boolean isShow) {
        tv_photo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public WithDrawalDialog setTvText2Visible(boolean isShow) {
        tv_camera.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public WithDrawalDialog setTvCancle(String cancle) {
        tv_cancel.setText(cancle);
        return this;
    }

    public WithDrawalDialog setTvCancleVisibility(boolean isShow) {
        tv_cancel.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public WithDrawalDialog setText1Listener(View.OnClickListener listener) {
        listenerPhoto = listener;
        return this;
    }

    public WithDrawalDialog setText2Listener(View.OnClickListener listener) {
        this.listenerCamera = listener;
        return this;
    }

    public WithDrawalDialog setText0Listener(View.OnClickListener listener) {
        this.listenerCancel = listener;
        return this;
    }
}