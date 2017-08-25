package com.xingbo.live.entity.model;

import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.entity.LoginUser;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class LoginUserModel extends BaseResponseModel {
    private LoginUser d;

    public LoginUser getD() {
        return d;
    }

    public void setD(LoginUser d) {
        this.d = d;
    }
}
