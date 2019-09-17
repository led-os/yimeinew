package com.chengzi.app.im.session.viewholder;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.chengzi.app.R;

public class MsgViewHolderUnBind extends MsgViewHolderBase {

    public MsgViewHolderUnBind(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.msg_viewholder_unbind_layout;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {

    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }


}
