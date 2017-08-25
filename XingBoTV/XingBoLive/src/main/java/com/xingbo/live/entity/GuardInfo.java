package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/3/16.
 */
public class GuardInfo {
    private GuardUser user;
    private List<GuardTimeAndPrice>months;

    public GuardUser getUser() {
        return user;
    }

    public void setUser(GuardUser user) {
        this.user = user;
    }

    public List<GuardTimeAndPrice> getMonths() {
        return months;
    }

    public void setMonths(List<GuardTimeAndPrice> months) {
        this.months = months;
    }
}
