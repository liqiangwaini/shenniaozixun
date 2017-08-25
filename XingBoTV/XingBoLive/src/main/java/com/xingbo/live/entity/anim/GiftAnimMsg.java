package com.xingbo.live.entity.anim;

/**
 * Created by WuJinZhou on 2015/12/18.
 */
public class GiftAnimMsg {
    private String id;
    private String flv;
    private String key;
    private String icon;

    public GiftAnimMsg(String id, String flv, String key, String icon) {
        this.id = id;
        this.flv = flv;
        this.key = key;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
