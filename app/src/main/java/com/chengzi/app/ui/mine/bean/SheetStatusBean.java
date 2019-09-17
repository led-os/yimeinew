package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class SheetStatusBean {
    /**
     * sheet_id : 1
     * is_chose : 1
     * answer_id : 1
     */

    private String sheet_id;   // 咨询单id
    private String is_chose;   // 是否选择咨询对象 (1-是 2-否)
    private String answer_id;  // 被选择id
    private String is_cancel;  // 是否手动取消 (1-是 2-否)

    public String getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(String sheet_id) {
        this.sheet_id = sheet_id;
    }

    public String getIs_chose() {
        return is_chose;
    }

    public void setIs_chose(String is_chose) {
        this.is_chose = is_chose;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getIs_cancel() {
        return !TextUtils.isEmpty(is_cancel) ? is_cancel : "2";
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }
}
