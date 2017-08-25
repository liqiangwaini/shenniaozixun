package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class HomeBaseInfo {
    private List<HomeTab>liveclass;
    private List<PicWall>picwalls;

    public List<HomeTab> getLiveclass() {
        return liveclass;
    }

    public void setLiveclass(List<HomeTab> liveclass) {
        this.liveclass = liveclass;
    }

    public List<PicWall> getPicwalls() {
        return picwalls;
    }

    public void setPicwalls(List<PicWall> picwalls) {
        this.picwalls = picwalls;
    }
}
