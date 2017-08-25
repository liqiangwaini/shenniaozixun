package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class GuardMsg extends BaseMsg {
    private GuardMsgBody data;

    public GuardMsgBody getData() {
        return data;
    }

    public void setData(GuardMsgBody data) {
        this.data = data;
    }
}
