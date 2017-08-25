package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserPayLogItem extends XingBoBaseItem {
    private String orderno;
    private String money;
    private String method;
    private String ctime;
    private String timetag;

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTimetag() {
        return timetag;
    }

    public void setTimetag(String timetag) {
        this.timetag = timetag;
    }
}
