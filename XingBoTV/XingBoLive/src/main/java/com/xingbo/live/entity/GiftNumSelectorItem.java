package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2015/12/3.
 */
public class GiftNumSelectorItem  {
    private int num;
    private String title;

    public GiftNumSelectorItem(int num, String title) {
        this.num = num;
        this.title = title;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
