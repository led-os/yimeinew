package com.chengzi.app.event;

public class SwitchMainTabEvent {

    private int position;

    public SwitchMainTabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
