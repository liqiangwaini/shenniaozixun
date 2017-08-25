package com.xingbo.live.entity.model;

import com.xingbo.live.entity.CloseLive;
import com.xingbo.live.entity.GiftType;
import com.xingbobase.http.BaseResponseModel;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/15.
 */
public class CloseLiveModel extends BaseResponseModel {
    private CloseLive d;

    public CloseLive getD() {
        return d;
    }

    public void setD(CloseLive d) {
        this.d = d;
    }
}
