package com.xingbo.live.eventbus;

/**
 * Created by WuJinZhou on 2016/2/27.
 */
public class    MFavoriteEvent {
    private boolean isFavorite=false;
    private String uid;

    public MFavoriteEvent(boolean isFavorite,String uid) {
        this.isFavorite = isFavorite;
        this.uid=uid;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
