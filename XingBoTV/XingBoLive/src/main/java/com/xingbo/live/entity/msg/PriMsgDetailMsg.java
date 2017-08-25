package com.xingbo.live.entity.msg;

import com.xingbo.live.entity.PriMsgDetail;

/**
 * Created by xingbo_szd on 2016/8/10.
 */
public class PriMsgDetailMsg extends BaseMsg {
    private PriMsgDetail data;

    public PriMsgDetail getData() {
        return data;
    }

    public void setData(PriMsgDetail data) {
        this.data = data;
    }
}
