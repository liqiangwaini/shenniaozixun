package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.BaseUser;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class MainUserModel extends BaseResponseModel {
    private BaseUser d;

    public BaseUser getD() {
        return d;
    }

    public void setD(BaseUser d) {
        this.d = d;
    }
}
