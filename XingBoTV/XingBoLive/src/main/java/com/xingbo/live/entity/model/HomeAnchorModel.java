package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.HomeAnchorPage;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class HomeAnchorModel extends BaseResponseModel {
    private HomeAnchorPage d;

    public HomeAnchorPage getD() {
        return d;
    }

    public void setD(HomeAnchorPage d) {
        this.d = d;
    }
}
