package com.chengzi.app.ui.mine.activity.onlinecases;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityWriteExperienceBindingImpl;

/**
 * 以往手术经历1/外伤经历2/变美经历3
 *
 * @ClassName:WriteExperienceActivity
 * @PackageName:com.yimei.app.ui.mine.activity.onlinecases
 * @Create On 2019/4/3 0003   16:10
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class WriteExperienceActivity extends BaseActivity<ActivityWriteExperienceBindingImpl, BaseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_write_experience;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String content_title = getIntent().getStringExtra("content_title");

        mBinding.topBar.setCenterText(title);
        if (!TextUtils.isEmpty(content_title)) {
            mBinding.etContent.setText(content_title);
        } else {
            mBinding.etContent.setHint(content);
        }

        ///监听输入长度
        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.tvCount.setText(mBinding.etContent.getText().length() + "");
            }
        });

        //确认
        mBinding.tvSure.setOnClickListener(v -> {
            Intent intent = getIntent();
            intent.putExtra("content", mBinding.etContent.getText().toString().trim());
            setResult(111, intent);
            finish();
        });
    }
}
