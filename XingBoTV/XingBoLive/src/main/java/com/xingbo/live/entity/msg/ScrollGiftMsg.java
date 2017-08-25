package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/1/29.
 */
public class ScrollGiftMsg extends BaseMsg {
    private ScrollGiftMsgBody data;

    public ScrollGiftMsgBody getData() {
        return data;
    }

    public void setData(ScrollGiftMsgBody data) {
        this.data = data;
    }
}
