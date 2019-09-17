package com.chengzi.app.ui.search;

public interface DisplayChangeRegistry {

    void register(OnDisplayChangeObserver observer);
    void unregister(OnDisplayChangeObserver observer);
}
