package com.xingbo.live.eventbus;

/**
 * Created by xingbo_szd on 2016/8/22.
 */
public class ReplyEvent {
    public ReplyEvent() {
    }

    public ReplyEvent(String nick) {
        this.nick = nick;
    }

    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
