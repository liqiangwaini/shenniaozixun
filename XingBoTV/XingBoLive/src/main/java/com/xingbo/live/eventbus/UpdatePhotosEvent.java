package com.xingbo.live.eventbus;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/26
 */
public class UpdatePhotosEvent {

    private String uid;

    public UpdatePhotosEvent(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
