package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class SystemMsg extends BaseMsg {
    private SystemMsgBody data;
    public SystemMsg(SystemMsgBody  data){
        this.data=data;

    }
    public SystemMsgBody getData() {
        return data;
    }

    public void setData(SystemMsgBody data) {
        this.data = data;
    }
}
