package com.chengzi.app.event;

import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;

public class ForumCollectEvent {

    private BaseForumViewModel viewModel;
    private String forumId;
    private int userType;
    private boolean collect;

    public ForumCollectEvent(BaseForumViewModel viewModel, String forumId, int userType, boolean collect) {
        this.viewModel = viewModel;
        this.forumId = forumId;
        this.userType = userType;
        this.collect = collect;
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

    public boolean isCollect() {
        return collect;
    }
}
