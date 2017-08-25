package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class AnchorLvUpMsg extends BaseMsg {
    private AnchorLvUpMsgBody data;
    public AnchorLvUpMsgBody getData() {
        return data;
    }

    public void setData(AnchorLvUpMsgBody data) {
        this.data = data;
    }

}
