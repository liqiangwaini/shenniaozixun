package com.xingbo.live.eventbus;

/**
 * Created by WuJinZhou on 2016/1/26.
 */
public class UserHeaderClickedEvent {
    private String uid;

    public UserHeaderClickedEvent(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
