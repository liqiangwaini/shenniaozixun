package com.xingbo.live.entity.model;

import com.xingbo.live.entity.StartLive;
import com.xingbobase.http.BaseResponseModel;

/**
 * 开始直播
 */
public class StartLiveModel extends BaseResponseModel {
    private StartLive d;

    public StartLive getD() {
        return d;
    }

    public void setD(StartLive d) {
        this.d = d;
    }
}
