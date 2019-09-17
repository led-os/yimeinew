package com.nevermore.oceans.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

public class NiceDialog extends BaseNiceDialog {
    private ViewConvertListener convertListener;
    OnDismissListener mDismissListener;

    public static NiceDialog init() {
        return new NiceDialog();
    }

    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }


    public NiceDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public NiceDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    public NiceDialog setOnDismissListener(OnDismissListener listener) {
        this.mDismissListener = listener;
        return this;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}