package com.xingbo.live.entity.model;

import com.xingbo.live.entity.MessageSystem;
import com.xingbo.live.entity.MessageSystemPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/31
 */
public class MessageSystemModel extends BaseResponseModel {
    private MessageSystemPage d;

    public MessageSystemPage getD() {
        return d;
    }

    public void setD(MessageSystemPage d) {
        this.d = d;
    }
}
