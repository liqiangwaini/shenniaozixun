package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class CancelUserBagItemMsg extends BaseMsg{
    private CancelUserBagItemBody data;
    public CancelUserBagItemBody getBody() {
        return data;
    }

    public void setBody(CancelUserBagItemBody fuser) {
        this.data = data;
    }
}
