package com.chengzi.app.ui.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandActivitySwitcherBinding;

import java.util.ArrayList;
import java.util.List;

public class PictureSwitcherActivity extends BaseActivity<HandActivitySwitcherBinding, BaseViewModel> implements ClickEventHandler {

     public static void start(Context context, ArrayList<String> pictureUrls,int index){
          Intent intent = new Intent(context,PictureSwitcherActivity.class);
          intent.putExtra(PICTURE_URLS,pictureUrls)
                  .putExtra(PICTURE_INDEX,index);
          context.startActivity(intent); 
     }
    
    public static final String PICTURE_URLS = "PICTURE_URLS";
    public static final String PICTURE_INDEX = "PICTURE_INDEX";
    //开票历史 需要显示标题
    private String title;

    private static final int REQUEST_CODE_PERM = 0x10;
    private List<String> pictureUrls;

    @Override
    public int getLayoutRes() {
        return R.layout.hand_activity_switcher;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator((R.drawable.hand_back_white));

        pictureUrls = (List<String>) getIntent().getSerializableExtra(PICTURE_URLS);
        int curIndex = getIntent().getIntExtra(PICTURE_INDEX, 0);
        title = getIntent().getStringExtra("title");

        if (!TextUtils.isEmpty(title)) {
            mBinding.tvIndex.setText(title);
            mBinding.toolbar.setBackgroundColor(Color.WHITE);
            mBinding.tvIndex.setTextColor(Color.BLACK);
            getSupportActionBar().setHomeAsUpIndicator((R.drawable.nav_fanhui));
        } else {
            mBinding.tvIndex.setText((curIndex + 1) + "/" + pictureUrls.size());
        }
        getWindow().peekDecorView().post(() -> {
            int measuredWidth = mBinding.viewpager.getMeasuredWidth();
            int measuredHeight = mBinding.viewpager.getMeasuredHeight();
            mBinding.viewpager.setAdapter(new PicturesAdapter(measuredWidth, measuredHeight, pictureUrls));
            mBinding.viewpager.setCurrentItem(curIndex, false);
        });

        if (TextUtils.isEmpty(title)) {
            mBinding.viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    mBinding.tvIndex.setText((position + 1) + "/" + pictureUrls.size());
                }
            });
        }

        mBinding.setListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleClick(View view, Object o) {
//        int id = view.getId();
//        if (id == R.id.image_download) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERM);
//            } else {
//                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    ToastUtils.showShort("无sd卡");
//                    return;
//                }
//                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                String url = pictureUrls.get(mBinding.viewpager.getCurrentItem());
//                if (url.contains("_mid")) {
//                    url = url.replace("_mid", "_big");
//                }
//                String fileName = null;
//                int indexOf = url.lastIndexOf("/");
//                if (indexOf > 0) {
//                    fileName = url.substring(indexOf + 1, url.length());
//                } else {
//                    fileName = EncryptUtils.encryptMD5ToString(url) + ".jpg";
//                }
//                File file = new File(dir,fileName);
//                mViewModel.downloadImge(url,file);
//            }
//        }
    }


    class PicturesAdapter extends PagerAdapter {

        private int maxWidth;
        private int maxHeight;
        private List<String> imgs;

        public PicturesAdapter(int maxWidth, int maxHeight, List<String> imgs) {
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs != null ? imgs.size() : 0;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setZoomable(false);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            photoView.setScaleType(ImageView.ScaleType.CENTER);
            ImageLoader.loadImage(container.getContext(), photoView, imgs.get(position), maxWidth, maxHeight);
            container.addView(photoView);
            photoView.setOnClickListener(v -> {
                onBackPressed();
            });
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }
}
