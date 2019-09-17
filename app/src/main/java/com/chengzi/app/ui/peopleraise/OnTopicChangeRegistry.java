package com.chengzi.app.ui.peopleraise;

import android.arch.lifecycle.Observer;

public interface OnTopicChangeRegistry<T> {

     void observeSearch(Observer<T> observer);

    void observeCategory(Observer<T> observer);
}
