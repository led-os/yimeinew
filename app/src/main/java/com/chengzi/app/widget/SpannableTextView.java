package com.chengzi.app.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.StaticLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

public class SpannableTextView extends AppCompatTextView {
    public SpannableTextView(Context context) {
        super(context);
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        StaticLayout layout = getStaticLayout();
//
//        if (layout == null) {
//            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//            layout = getStaticLayout();
//        }
//
//        Field field = null;
//        if (layout != null) {
//            try {
//                field = StaticLayout.class.getDeclaredField("mMaximumVisibleLineCount");
//                field.setAccessible(true);
//                field.setInt(layout, getMaxLines());
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (layout != null && field != null) {
//            try {
//                field.setInt(layout, Integer.MAX_VALUE);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private StaticLayout getStaticLayout(){
        StaticLayout layout = null;
        try {
            Field staticField = DynamicLayout.class.getDeclaredField("sStaticLayout");
            staticField.setAccessible(true);
            layout = (StaticLayout) staticField.get(DynamicLayout.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return layout;
    }
}
