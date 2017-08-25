package com.xingbo.live.entity.model;

import com.xingbo.live.entity.MessagePrivatePage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/7/29
 */
public class MessagePrivateModel extends BaseResponseModel {
 private MessagePrivatePage d;

    public MessagePrivatePage getD() {
        return d;
    }

    public void setD(MessagePrivatePage d) {
        this.d = d;
    }
}
