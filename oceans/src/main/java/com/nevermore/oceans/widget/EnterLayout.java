package com.nevermore.oceans.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nevermore.oceans.R;
import com.nevermore.oceans.uits.CommonUtils;


/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class EnterLayout extends LinearLayout {

    private TextView tvSubject;
    private TextView tvContent;
    private ImageView ivArrow;
    private String content;

    public EnterLayout(Context context) {
        this(context, null);
    }

    public EnterLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnterLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        LayoutInflater.from(context).inflate(R.layout.edit_linear_layout, this);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EnterLayout);

        boolean hide = typedArray.getBoolean(R.styleable.EnterLayout_hideArrow, false);
        if (hide) {
            hideArrow();
        }

//        int color = typedArray.getColor(R.styleable.EnterLayout_text_color, 0xffffffff);
//        tvContent.setTextColor(color);
//        tvSubject.setTextColor(color);
        float dimension = typedArray.getDimensionPixelSize(R.styleable.EnterLayout_text_size, 0);
        if (dimension != 0) {
            tvSubject.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);
        }

        String content = typedArray.getString(R.styleable.EnterLayout_text_content);
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        String textColor = typedArray.getString(R.styleable.EnterLayout_textColor);
        if (!TextUtils.isEmpty(textColor)) {
            tvSubject.setTextColor(typedArray.getColor(R.styleable.EnterLayout_textColor, Color.parseColor("#333333")));
            tvContent.setTextColor(typedArray.getColor(R.styleable.EnterLayout_textColor, Color.parseColor("#333333")));
        }
        String text_color = typedArray.getString(R.styleable.EnterLayout_text_color);
        if (!TextUtils.isEmpty(text_color)) {
            tvContent.setTextColor(typedArray.getColor(R.styleable.EnterLayout_text_color, Color.parseColor("#333333")));
        }

        String textSize = typedArray.getString(R.styleable.EnterLayout_textSize);
        if (!TextUtils.isEmpty(textSize)) {
//            tvSubject.setTextSize(typedArray.getFloat(R.styleable.EnterLayout_textSize, 15));
        }
        String string = typedArray.getString(R.styleable.EnterLayout_textSubject);
        if (!TextUtils.isEmpty(string)) {
            tvSubject.setText(string);
        }
        String rightTextColor = typedArray.getString(R.styleable.EnterLayout_rightTextColor);
        if (!TextUtils.isEmpty(rightTextColor)) {
            tvContent.setTextColor(typedArray.getColor(R.styleable.EnterLayout_rightTextColor, Color.parseColor("#333333")));
        }
        String rightTextSize = typedArray.getString(R.styleable.EnterLayout_rightTextSize);
        if (!TextUtils.isEmpty(rightTextSize)) {
            tvContent.setTextSize(
                    typedArray.getFloat(R.styleable.EnterLayout_textSize, 14));
        }
        typedArray.recycle();
    }

    public void hideArrow() {
        ivArrow.setVisibility(INVISIBLE);

    }

    public void setIvArrow(int res) {
        ivArrow.setImageResource(res);

    }

    public String getContent() {
        return tvContent.getText().toString().trim();
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setSubject(String subject) {
        tvSubject.setText(subject);
    }

}
