package com.xingbo.live.eventbus;

import com.xingbo.live.entity.MainUser;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class UserInfoEvent {
    private MainUser userinfo;

    public UserInfoEvent(MainUser userinfo) {
        this.userinfo = userinfo;
    }

    public MainUser getUserInfo() {
        return userinfo;
    }

    public void setCurrentCoin(MainUser userinfo) {
        this.userinfo = userinfo;
    }
}
