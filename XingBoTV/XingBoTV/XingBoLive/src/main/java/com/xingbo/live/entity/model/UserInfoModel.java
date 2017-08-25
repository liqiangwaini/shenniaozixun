package com.xingbo.live.entity.model;

import com.xingbo.live.entity.MainUser;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class UserInfoModel extends BaseResponseModel {
    private MainUser d;

    public MainUser getD() {
        return d;
    }

    public void setD(MainUser d) {
        this.d = d;
    }
}
