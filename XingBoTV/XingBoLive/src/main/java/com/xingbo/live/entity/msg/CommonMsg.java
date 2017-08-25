package com.xingbo.live.entity.msg;

/**
 * 普通聊天消息
 * Created by WuJinZhou on 2015/12/8.
 */
public class CommonMsg extends BaseMsg {
    private CommonMsgBody data;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CommonMsgBody getData() {
        return data;
    }

    public void setData(CommonMsgBody data) {
        this.data = data;
    }
}
