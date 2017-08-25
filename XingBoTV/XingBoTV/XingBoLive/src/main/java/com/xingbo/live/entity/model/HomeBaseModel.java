package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.HomeBaseInfo;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class HomeBaseModel extends BaseResponseModel {
    private HomeBaseInfo d;

    public HomeBaseInfo getD() {
        return d;
    }

    public void setD(HomeBaseInfo d) {
        this.d = d;
    }
}
