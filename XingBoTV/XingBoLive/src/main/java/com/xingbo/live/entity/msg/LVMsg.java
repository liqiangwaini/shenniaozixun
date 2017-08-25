package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2015/12/10.
 */
public class LVMsg extends BaseMsg {
    private LVMsgBody data;

    public LVMsgBody getData() {
        return data;
    }

    public void setData(LVMsgBody data) {
        this.data = data;
    }
}
