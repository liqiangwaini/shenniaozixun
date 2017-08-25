package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.RoomUserInfo;

/**
 * Created by WuJinZhou on 2016/1/26.
 */
public class PopWinRoomUserModel extends BaseResponseModel {
    private RoomUserInfo d;

    public RoomUserInfo getD() {
        return d;
    }

    public void setD(RoomUserInfo d) {
        this.d = d;
    }
}
