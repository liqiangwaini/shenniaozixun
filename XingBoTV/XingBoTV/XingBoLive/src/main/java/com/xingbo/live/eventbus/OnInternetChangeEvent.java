package com.xingbo.live.eventbus;

/**
 * Created by WuJinZhou on 2016/3/10.
 */
public class OnInternetChangeEvent {

    private String  netName=null;

    public OnInternetChangeEvent(String netName) {
        this.netName = netName;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }
}
