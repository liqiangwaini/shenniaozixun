package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UserSendLogListPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserSendLogListModel extends BaseResponseModel {
    private UserSendLogListPage d;

    public UserSendLogListPage getD() {
        return d;
    }

    public void setD(UserSendLogListPage d) {
        this.d = d;
    }
}
