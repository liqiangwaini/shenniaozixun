package com.xingbo.live.eventbus;

import com.xingbo.live.entity.Gift;

/**
 * Created by WuJinZhou on 2015/12/6.
 */
public class GiftItemSelectedEvent {

    private int position;

    private Gift giftItem;

    public GiftItemSelectedEvent(Gift giftItem) {
        this.giftItem = giftItem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Gift getGiftItem() {
        return giftItem;
    }

    public void setGiftItem(Gift giftItem) {
        this.giftItem = giftItem;
    }
}
