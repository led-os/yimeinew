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
/**
 * 圆角ImageView
 * @ClassName:RoundImageView

 * @PackageName:com.yimei.app.widget

 * @Create On 2019/4/8 0008   13:52

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class RoundImageView extends AppCompatImageView {

    private static final float RADIUS_DEFAULT = SizeUtils.dp2px(4);

    public RoundImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        float radius = typedArray.getFloat(R.styleable.RoundImageView_image_radius, RADIUS_DEFAULT);
        typedArray.recycle();
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),radius);
            }
        });
    }
}
