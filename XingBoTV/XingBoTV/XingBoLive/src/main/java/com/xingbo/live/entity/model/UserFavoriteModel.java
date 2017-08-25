package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.UserFavoritePage;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFavoriteModel extends BaseResponseModel {
    private UserFavoritePage d;

    public UserFavoritePage getD() {
        return d;
    }

    public void setD(UserFavoritePage d) {
        this.d = d;
    }
}
