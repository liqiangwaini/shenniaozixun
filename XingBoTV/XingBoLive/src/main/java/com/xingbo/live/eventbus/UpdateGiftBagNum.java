package com.xingbo.live.eventbus;

/**
 *
 * 包裹送出后的通知体
 * Created by WuJinZhou on 2015/12/12.
 */
public class UpdateGiftBagNum {
    private String id;
    private int num;

    public UpdateGiftBagNum(String id, int num) {
        this.id = id;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
