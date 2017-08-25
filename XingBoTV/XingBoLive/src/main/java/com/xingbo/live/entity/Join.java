package com.xingbo.live.entity;

import com.xingbo.live.entity.msg.MsgFUser;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class Join {
    private MsgFUser fuser;
    private int liveonlines;
    private String reconnect;

    public MsgFUser getFuser() {
        return fuser;
    }

    public void setFuser(MsgFUser fuser) {
        this.fuser = fuser;
    }

    public int getLiveonlines() {
        return liveonlines;
    }

    public void setLiveonlines(int liveonlines) {
        this.liveonlines = liveonlines;
    }

    public String getReconnect() {
        return reconnect;
    }

    public void setReconnect(String reconnect) {
        this.reconnect = reconnect;
    }
}
