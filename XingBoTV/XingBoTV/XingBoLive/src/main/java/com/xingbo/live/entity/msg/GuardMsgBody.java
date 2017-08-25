package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class GuardMsgBody {
    private MsgFUser fuser;
    private int lvl;
    private int num;

    public MsgFUser getFuser() {
        return fuser;
    }

    public void setFuser(MsgFUser fuser) {
        this.fuser = fuser;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
