package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.MainUser;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserHomeModel extends BaseResponseModel {
    private MainUser d;

    public MainUser getD() {
        return d;
    }

    public void setD(MainUser d) {
        this.d = d;
    }
}
