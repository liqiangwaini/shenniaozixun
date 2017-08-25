package com.xingbo.live.entity;

import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.MsgTUser;

/**
 * Created by xingbo_szd on 2016/8/10.
 */
public class PriMsgDetail {
    private MsgFUser fuser;
    private MsgTUser tuser;
    private String msg;

    public MsgFUser getFuser() {
        return fuser;
    }

    public void setFuser(MsgFUser fuser) {
        this.fuser = fuser;
    }

    public MsgTUser getTuser() {
        return tuser;
    }

    public void setTuser(MsgTUser tuser) {
        this.tuser = tuser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
