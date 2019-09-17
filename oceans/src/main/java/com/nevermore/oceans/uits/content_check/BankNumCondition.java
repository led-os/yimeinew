package com.nevermore.oceans.uits.content_check;

import android.text.TextUtils;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.nevermore.oceans.uits.BankUtils;

/**
 * @Desc: 检验银行卡是否合法
 * @ClassName:BankNumCondition
 * @PackageName:com.nevermore.oceans.uits.content_check
 * @Create On 2019/4/26 0026
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class BankNumCondition implements IContentChecker.Condition {


    @Override
    public boolean match(IContentChecker.Body body) {
        return !TextUtils.isEmpty(body.getContent()) && (BankUtils.checkBankCard(body.getContent().toString()));
    }

    @Override
    public void showTips(IContentChecker.Body body) {
        ToastUtils.showShort("银行卡号不正确");
    }
}
