package com.xingbo.live.eventbus;

import com.xingbo.live.entity.UserRoomInfo;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class RoomUserInfoEvent {
    private UserRoomInfo userinfo;

    public RoomUserInfoEvent(UserRoomInfo userinfo) {
        this.userinfo = userinfo;
    }

    public UserRoomInfo getUserInfo() {
        return userinfo;
    }

    public void setCurrentCoin(UserRoomInfo userinfo) {
        this.userinfo = userinfo;
    }
}
