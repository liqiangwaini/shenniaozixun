package com.xingbo.live.entity.model;

import com.xingbo.live.entity.MessagePrivateDetailPage;
import com.xingbo.live.entity.MessagePrivatePage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class MessagePrivateDetailModel extends BaseResponseModel {
    private MessagePrivateDetailPage d;

    public MessagePrivateDetailPage getD() {
        return d;
    }

    public void setD(MessagePrivateDetailPage d) {
        this.d = d;
    }
}
