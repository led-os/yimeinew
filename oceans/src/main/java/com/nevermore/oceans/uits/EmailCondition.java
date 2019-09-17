package com.nevermore.oceans.uits;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.nevermore.oceans.uits.content_check.IContentChecker;

public class EmailCondition implements IContentChecker.Condition {
    @Override
    public boolean match(IContentChecker.Body body) {
        return RegexUtils.isEmail(body.getContent());
    }

    @Override
    public void showTips(IContentChecker.Body body) {
        ToastUtils.showShort("邮箱格式不正确！");
    }
}
