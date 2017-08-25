package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UserIncomeData;
import com.xingbobase.http.BaseResponseModel;

/**
 * 用户收益
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeModel extends BaseResponseModel {
    private UserIncomeData d;

    public UserIncomeData getD() {
        return d;
    }

    public void setD(UserIncomeData d) {
        this.d = d;
    }
}
