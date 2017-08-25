package com.xingbo.live.entity.model;

import com.xingbo.live.entity.UserPhotosPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class UserPhotosModel extends BaseResponseModel {
     private UserPhotosPage d;

    public UserPhotosPage getD() {
        return d;
    }

    public void setD(UserPhotosPage d) {
        this.d = d;
    }
}
