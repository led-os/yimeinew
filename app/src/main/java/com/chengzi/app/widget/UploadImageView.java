package com.chengzi.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.handongkeji.utils.CommonUtils;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;

/**
 * 认证需要用到的上传照片封装
 *
 * @ClassName:UploadImageView
 * @PackageName:com.yimei.app.widget
 * @Create On 2019/4/10 0010   13:46
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class UploadImageView extends ConstraintLayout {

    private static final String TAG = "UploadImageView=";
    private RatioImageView iv_image;
    private TextView tv_text;
    private ImageView iv_close;

    public UploadImageView(Context context) {
        this(context, null);
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_upload_image, this);
//        cl_image = findViewById(R.id.cl_image);
        iv_image = findViewById(R.id.iv_image);
        tv_text = findViewById(R.id.tv_text);
        iv_close = findViewById(R.id.iv_close);

        iv_close.setVisibility(GONE);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UploadImageView);

        int dimension = typedArray.getDimensionPixelSize(R.styleable.UploadImageView_textSize, 13);
        if (dimension != 0) {
            tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.sp2px(context, dimension));
        }

        String content = typedArray.getString(R.styleable.UploadImageView_text);
        if (!TextUtils.isEmpty(content)) {
            tv_text.setText(content);
        }
        String textColor = typedArray.getString(R.styleable.UploadImageView_textColor);
        if (!TextUtils.isEmpty(textColor)) {
            tv_text.setTextColor(typedArray.getColor(R.styleable.UploadImageView_textColor, Color.parseColor("#333333")));
        }
        int image = typedArray.getResourceId(R.styleable.UploadImageView_image, R.drawable.shangchuanzhaopian);
        iv_image.setImageResource(image);
        typedArray.recycle();

        iv_close.setOnClickListener(v -> {
            iv_image.setTag(null);
            iv_image.setImageResource(R.drawable.shangchuanzhaopian);
            iv_close.setVisibility(GONE);
        });
    }

    public String getText() {
        return tv_text.getText().toString().trim();
    }

    public void setText(String content) {
        tv_text.setText(content);
    }

    public void setIv_image(String path) {
//        iv_image.setTag(null);
        ImageLoader.loadImage(iv_image, path);
        iv_close.setVisibility(VISIBLE);
//        iv_image.setTag(path);
        Log.i(TAG, "setIv_image: " + path);
    }

    public  ImageView getIv_image() {
        return iv_image;
    }

    public String getIvImageTag() {
        Log.i(TAG, "getIvImageTag: " + iv_image.getTag().toString());
        return iv_image.getTag().toString();
    }
}