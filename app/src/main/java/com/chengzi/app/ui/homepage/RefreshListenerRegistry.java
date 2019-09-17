package com.chengzi.app.ui.homepage;

public interface RefreshListenerRegistry {

    void register(OnRefreshListener listener);

    void unRegister(OnRefreshListener listener);

    void finishRefresh();
}
