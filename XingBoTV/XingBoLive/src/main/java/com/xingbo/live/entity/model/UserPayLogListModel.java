package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UserPayLogListPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserPayLogListModel extends BaseResponseModel {
    private UserPayLogListPage d;

    public UserPayLogListPage getD() {
        return d;
    }

    public void setD(UserPayLogListPage d) {
        this.d = d;
    }
}
