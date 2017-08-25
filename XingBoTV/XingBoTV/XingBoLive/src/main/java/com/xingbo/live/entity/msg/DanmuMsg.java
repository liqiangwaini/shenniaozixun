package com.xingbo.live.entity.msg;

import com.xingbo.live.entity.DanmuItem;

/**
 * Created by xingbo_szd on 2016/7/23.
 */
public class DanmuMsg  extends  BaseMsg{

    private DanmuItem data;

    public DanmuItem getData() {
        return data;
    }

    public void setData(DanmuItem data) {
        this.data = data;
    }
}
