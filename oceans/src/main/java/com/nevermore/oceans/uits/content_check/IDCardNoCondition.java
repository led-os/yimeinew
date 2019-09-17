package com.nevermore.oceans.uits.content_check;

import android.text.TextUtils;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * 验证15位或者18位身份证号
 * Created by Administrator on 2018/2/2 0002.
 */

public class IDCardNoCondition implements IContentChecker.Condition {


    @Override
    public boolean match(IContentChecker.Body body) {
        return !TextUtils.isEmpty(body.getContent()) && (RegexUtils.isIDCard15(body.getContent()) || RegexUtils.isIDCard18(body.getContent()));
    }

    @Override
    public void showTips(IContentChecker.Body body) {
        ToastUtils.showShort("身份证号不正确");
    }
}
