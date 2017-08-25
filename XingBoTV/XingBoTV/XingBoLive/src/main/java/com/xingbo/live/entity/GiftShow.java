package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/7/26.
 */
public class GiftShow {
    public GiftShow() {
    }

    public GiftShow(SendGift sendGift, long time) {
        this.time = time;
        this.sendGift = sendGift;
    }

    public SendGift sendGift;
    public long time;
}
