package com.xingbo.live.entity.msg;

import com.xingbo.live.entity.Gift;

/**
 * Created by WuJinZhou on 2016/1/29.
 */
public class ScrollGiftMsgBody {
    private MsgFUser fuser;
    private MsgTUser tuser;
    private Gift gift;
    private int num;
    private int sendtime;
    private long endtime;


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

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSendtime() {
        return sendtime;
    }

    public void setSendtime(int sendtime) {
        this.sendtime = sendtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }
}
