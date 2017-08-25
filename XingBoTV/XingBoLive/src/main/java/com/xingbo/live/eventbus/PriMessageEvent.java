package com.xingbo.live.eventbus;

/**
 * Created by xingbo_szd on 2016/8/22.
 */
public class PriMessageEvent {
    private String id;
    private String nick;

    public PriMessageEvent() {
    }

    public PriMessageEvent(String id, String nick) {
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
