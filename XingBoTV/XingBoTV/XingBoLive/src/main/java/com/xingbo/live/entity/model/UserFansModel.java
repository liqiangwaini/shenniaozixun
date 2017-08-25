package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.UserFansPage;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFansModel extends BaseResponseModel {
    private UserFansPage d;

    public UserFansPage getD() {
        return d;
    }

    public void setD(UserFansPage d) {
        this.d = d;
    }
}
