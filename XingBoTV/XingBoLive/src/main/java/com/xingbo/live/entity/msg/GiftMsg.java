package com.xingbo.live.entity.msg;


import com.xingbo.live.entity.SendGift;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class GiftMsg extends BaseMsg {
    private SendGift data;

    public SendGift getData() {
        return data;
    }

    public void setData(SendGift data) {
        this.data = data;
    }
}
