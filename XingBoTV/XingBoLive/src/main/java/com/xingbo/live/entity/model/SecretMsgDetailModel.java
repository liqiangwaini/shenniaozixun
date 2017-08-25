package com.xingbo.live.entity.model;

import com.xingbo.live.entity.SecretMsgDetail;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by xingbo_szd on 2016/7/29.
 */
public class SecretMsgDetailModel extends BaseResponseModel {
    private SecretMsgDetail d;

    public SecretMsgDetail getD() {
        return d;
    }

    public void setD(SecretMsgDetail d) {
        this.d = d;
    }
}
