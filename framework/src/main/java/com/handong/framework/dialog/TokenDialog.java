package com.handong.framework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;

import com.blankj.utilcode.util.SizeUtils;
import com.handongkeji.framework.R;
import com.handongkeji.framework.databinding.DialogTokenLayoutBinding;

public class TokenDialog extends BaseDialog {

    private static boolean showing;

    private DialogTokenLayoutBinding mBinding;
    private String message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_token_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.llOut.setClipToOutline(true);
        mBinding.llOut.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int dp2px = SizeUtils.dp2px(7);
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), dp2px);
            }
        });

        mBinding.tvConfirm.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onClick(v);
            }
            dismissAllowingStateLoss();
        });
        mBinding.tvMessage.setText(message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new NonBackDialog(this.getActivity(), this.getTheme());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private class NonBackDialog extends Dialog {

        public NonBackDialog(@NonNull Context context) {
            super(context);
        }

        public NonBackDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        public void onBackPressed() {
            //  禁用返回
//            super.onBackPressed();
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private View.OnClickListener confirmListener;

    public void setConfirmListener(View.OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!showing) {
            super.show(manager, tag);
            showing = true;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        showing = false;
    }

}
