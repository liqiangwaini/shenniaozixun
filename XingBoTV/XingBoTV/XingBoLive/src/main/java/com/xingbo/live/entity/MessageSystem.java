package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/31
 */
public class MessageSystem extends XingBoBaseItem {

    private String id;//消息id
    private String msg;//消息内容
    private String ctime;//发送时间
    private String href;
    private boolean isShow=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }
}
