package com.xingbo.live.entity.msg;


import com.xingbo.live.entity.Join;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class JoinMsg extends BaseMsg {
    private Join data;

    public Join getData() {
        return data;
    }

    public void setData(Join data) {
        this.data = data;
    }
}
