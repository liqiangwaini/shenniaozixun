package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.GiftType;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/15.
 */
public class GiftPanModel extends BaseResponseModel {
    private List<GiftType>d;

    public List<GiftType> getD() {
        return d;
    }

    public void setD(List<GiftType> d) {
        this.d = d;
    }
}
