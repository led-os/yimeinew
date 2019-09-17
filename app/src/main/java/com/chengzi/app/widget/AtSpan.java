package com.chengzi.app.widget;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.style.ForegroundColorSpan;

import com.chengzi.app.ui.mine.bean.FollowBean;

public class AtSpan extends ForegroundColorSpan {

    public AtSpan(int color, FollowBean atUser) {
        super(color);
        this.atUser = atUser;
    }

    public AtSpan(@NonNull Parcel src) {
        super(src);
    }

    private FollowBean atUser;


    public CharSequence getUserName() {
        return atUser == null ? "" : "@" + atUser.getName();
    }

    public String getUserId(){
        return atUser == null?"":atUser.getUser_id();
    }
    public int getUserType(){
        return atUser == null?0:atUser.getUser_type();
    }
}
