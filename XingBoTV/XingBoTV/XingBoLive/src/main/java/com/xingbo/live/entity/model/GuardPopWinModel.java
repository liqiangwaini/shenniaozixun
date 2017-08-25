package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.GuardInfo;

/**
 * Created by WuJinZhou on 2016/3/16.
 */
public class GuardPopWinModel extends BaseResponseModel {
    private GuardInfo d;

    public GuardInfo getD() {
        return d;
    }

    public void setD(GuardInfo d) {
        this.d = d;
    }
}
