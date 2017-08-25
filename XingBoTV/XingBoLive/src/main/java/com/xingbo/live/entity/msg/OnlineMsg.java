package com.xingbo.live.entity.msg;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/3/22.
 */
public class OnlineMsg extends BaseMsg {
    private List<MsgFUser>data;

    public List<MsgFUser> getData() {
        return data;
    }

    public void setData(List<MsgFUser> data) {
        this.data = data;
    }
}
