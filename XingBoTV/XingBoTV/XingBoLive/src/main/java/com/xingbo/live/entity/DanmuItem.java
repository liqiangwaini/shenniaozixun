package com.xingbo.live.entity;

import com.xingbo.live.entity.msg.MsgFUser;

/**
 * Created by xingbo_szd on 2016/7/23.
 */
public class DanmuItem {
    private MsgFUser fuser;
    private String word;

    public MsgFUser getFuser() {
        return fuser;
    }

    public void setFuser(MsgFUser fuser) {
        this.fuser = fuser;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
