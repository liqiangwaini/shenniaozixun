package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserSendLogItem extends XingBoBaseItem {
    private String title;
    private String consume;
    private String ctime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
