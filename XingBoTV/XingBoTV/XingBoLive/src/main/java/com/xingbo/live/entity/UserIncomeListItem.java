package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeListItem extends XingBoBaseItem {
    private String total;
    private String dd;
    private String timetag;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = "收入 " + total + " 星钻";
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd.replaceAll("-","\\.");
    }

    public String getTimetag() {
        return timetag;
    }

    public void setTimetag(String timetag) {
        this.timetag = timetag;
    }
}
