package com.chengzi.app.ui.search;

public interface OnDisplayChangeObserver {

    void onDisplayChange(Display display);

    enum Display{
        LIST,//  列表显示，一行一个
        GRID;//  网格显示，一行两个
    }
}
