package com.xingbo.live.entity;

import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.MsgTUser;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class SendGift {
    private MsgFUser fuser;
    private MsgTUser tuser;
    private Gift gift;
    private int num;
    private int effect_type;   //1普通礼物  2连送
    private int combo;  //送礼记录里当前应当显示数量

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

    public int getEffect_type() {
        return effect_type;
    }

    public void setEffect_type(int effect_type) {
        this.effect_type = effect_type;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }
}
