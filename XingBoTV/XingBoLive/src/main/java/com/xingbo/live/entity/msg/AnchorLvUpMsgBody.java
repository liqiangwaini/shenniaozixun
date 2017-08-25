package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class AnchorLvUpMsgBody {
    private MsgFUser fuser;
    private int lvl;
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
}
