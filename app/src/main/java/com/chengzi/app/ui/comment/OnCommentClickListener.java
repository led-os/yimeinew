package com.chengzi.app.ui.comment;

import android.view.View;

import com.chengzi.app.ui.comment.bean.CommentBean;

public interface OnCommentClickListener {

    void onContent(View view, CommentBean bean);

    void onDelete(View view, CommentBean bean);

}
