package com.xingbo.live.eventbus;

/**
 * Created by xingbo_szd on 2016/7/22.
 */
public class DanmuEvent {
    private String msg;
    DanmuEvent(){}
    public DanmuEvent(String msg){
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
