package com.xingbo.live.eventbus;

/**
 * Created by WuJinZhou on 2016/1/31.
 */
public class LiveStopAndStartEvent {
    public final static int VIDEO_STOP=0x11;
    public final static int VIDEO_PLAY=0x12;
    private int state=VIDEO_STOP;

    public LiveStopAndStartEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
