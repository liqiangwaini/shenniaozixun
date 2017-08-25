package com.xingbo.live.entity.msg;

/**
 * 普通聊天消息体
 * Created by WuJinZhou on 2015/12/8.
 */
public class CommonMsgBody {
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
