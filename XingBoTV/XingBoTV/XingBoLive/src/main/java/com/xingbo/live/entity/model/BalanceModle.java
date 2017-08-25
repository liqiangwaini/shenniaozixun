package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;

/**
 * Created by 123 on 2016/6/2.
 *
 * @package com.xingbo.live.entity.model
 * @description 描述 最新星币余额 数据
 */
public class BalanceModle extends BaseResponseModel {
    private String d;

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
