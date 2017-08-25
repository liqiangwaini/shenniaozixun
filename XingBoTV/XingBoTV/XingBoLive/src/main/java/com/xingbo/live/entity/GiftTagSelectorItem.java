package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/1/18.
 */
public class GiftTagSelectorItem {
    private String id;
    private String nick;

    public GiftTagSelectorItem(String id, String nick) {
        this.id = id;
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
