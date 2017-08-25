package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class UserRichUpMsg extends BaseMsg {
    private UserRichUpMsgBody data;

    public UserRichUpMsgBody getData() {
        return data;
    }

    public void setData(UserRichUpMsgBody data) {
        this.data = data;
    }
}
