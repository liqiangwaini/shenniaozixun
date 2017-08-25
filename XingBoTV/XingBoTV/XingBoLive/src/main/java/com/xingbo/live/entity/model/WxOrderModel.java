package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.WxPayReq;

/**
 * Created by Terry on 2016/3/23.
 */
public class WxOrderModel extends BaseResponseModel {
    private WxPayReq d;

    public WxPayReq getD() {
        return d;
    }

    public void setD(WxPayReq d) {
        this.d = d;
    }
}
