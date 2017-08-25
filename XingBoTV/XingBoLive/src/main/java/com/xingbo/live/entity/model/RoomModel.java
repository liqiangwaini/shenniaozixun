package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.RoomInfo;

/**
 * Created by WuJinZhou on 2016/1/15.
 */
public class RoomModel extends BaseResponseModel{
    private RoomInfo d;

    public RoomInfo getD() {
        return d;
    }

    public void setD(RoomInfo d) {
        this.d = d;
    }
}
