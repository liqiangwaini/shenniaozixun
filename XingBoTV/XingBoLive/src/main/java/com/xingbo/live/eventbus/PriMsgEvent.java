package com.xingbo.live.eventbus;

import com.xingbo.live.entity.msg.PriMsgDetailMsg;

/**
 * Created by xingbo_szd on 2016/8/10.
 */
public class PriMsgEvent {

    private PriMsgDetailMsg priMsgDetail;

    public  PriMsgEvent() {
    }

    public PriMsgEvent(PriMsgDetailMsg priMsgDetail) {
        this.priMsgDetail = priMsgDetail;
    }

    public PriMsgDetailMsg getPriMsgDetail() {
        return priMsgDetail;
    }

    public void setPriMsgDetail(PriMsgDetailMsg priMsgDetail) {
        this.priMsgDetail = priMsgDetail;
    }
}
