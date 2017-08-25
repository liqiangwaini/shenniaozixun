package com.xingbo.live.entity.model;

import com.xingbo.live.entity.ContributionPage;
import com.xingbo.live.entity.Mute;
import com.xingbobase.http.BaseResponseModel;

/**
 * 禁言
 */
public class MuteModle extends BaseResponseModel {
    private Mute d;

    public MuteModle(Mute d) {
        this.d = d;
    }

    public Mute getD() {
        return d;
    }

    public void setD(Mute d) {
        this.d = d;
    }
}
