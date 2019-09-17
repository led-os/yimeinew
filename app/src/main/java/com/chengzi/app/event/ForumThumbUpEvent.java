package com.chengzi.app.event;

import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;

public class ForumThumbUpEvent {

    private BaseForumViewModel viewModel;

    private String forumId;
    private int userType;
    private boolean thumbUp;

    public ForumThumbUpEvent(BaseForumViewModel viewModel, String forumId, int userType, boolean thumbUp) {
        this.viewModel = viewModel;
        this.forumId = forumId;
        this.userType = userType;
        this.thumbUp = thumbUp;
    }

    public BaseForumViewModel getViewModel() {
        return viewModel;
    }

    public String getForumId() {
        return forumId;
    }

    public int getUserType() {
        return userType;
    }

    public boolean isThumbUp() {
        return thumbUp;
    }

}
