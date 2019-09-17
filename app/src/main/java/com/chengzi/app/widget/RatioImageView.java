package com.chengzi.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.blankj.utilcode.util.SizeUtils;
import com.chengzi.app.R;


public class RatioImageView extends AppCompatImageView {

    private static final int RADIUS_DEFAULT = SizeUtils.dp2px(4);

    private int mWidthRatio;
    private int mHeightRatio;

    public RatioImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Hand_RatioImageView);
        mWidthRatio = typedArray.getInteger(R.styleable.Hand_RatioImageView_hand_ratio_width, 0);
        mHeightRatio = typedArray.getInteger(R.styleable.Hand_RatioImageView_hand_ratio_height, 0);
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        float radius = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_image_radius, RADIUS_DEFAULT);
        typedArray.recycle();
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),radius);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mWidthRatio > 0 && mHeightRatio > 0) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            //  345:189
            int height = (int) (width * mHeightRatio * 1f / mWidthRatio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
