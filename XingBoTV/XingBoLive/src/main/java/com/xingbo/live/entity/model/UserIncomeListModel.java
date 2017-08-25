package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UserIncomeListPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeListModel extends BaseResponseModel {

    private UserIncomeListPage d;

    public UserIncomeListPage getD() {
        return d;
    }

    public void setD(UserIncomeListPage d) {
        this.d = d;
    }
}
