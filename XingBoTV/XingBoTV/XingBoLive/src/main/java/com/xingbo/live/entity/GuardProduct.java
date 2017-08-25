package com.xingbo.live.entity;

/**
 * 守护权限项
 * Created by WuJinZhou on 2016/3/16.
 */
public class GuardProduct {
    private String name;
    private int resId;

    public GuardProduct(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
