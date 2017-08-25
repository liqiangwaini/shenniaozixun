package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UpdateInfo;
import com.xingbo.live.eventbus.UpdatePhotosEvent;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/29
 */
public class UpdateInfoModel extends BaseResponseModel {
    private UpdateInfo d;

    public UpdateInfo getD() {
        return d;
    }

    public void setD(UpdateInfo d) {
        this.d = d;
    }
}
