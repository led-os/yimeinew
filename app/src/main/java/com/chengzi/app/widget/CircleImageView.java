package com.chengzi.app.widget;

import android.content.Context;
import android.graphics.Outline;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CircleImageView extends AppCompatImageView {

    private ViewOutlineProvider outlineProvider;

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipToOutline(true);
        outlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            }
        };
        setOutlineProvider(outlineProvider);
    }

}
