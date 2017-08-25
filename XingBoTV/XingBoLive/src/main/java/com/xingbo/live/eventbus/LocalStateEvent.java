package com.xingbo.live.eventbus;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/2
 */
public class LocalStateEvent {
    private int flag;

    public LocalStateEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
