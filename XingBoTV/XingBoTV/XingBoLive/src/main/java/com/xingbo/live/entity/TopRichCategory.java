package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class TopRichCategory {
    private List<TopRichItem>day;
    private List<TopRichItem>week;
    private List<TopRichItem>month;

    public List<TopRichItem> getDay() {
        return day;
    }

    public void setDay(List<TopRichItem> day) {
        this.day = day;
    }

    public List<TopRichItem> getWeek() {
        return week;
    }

    public void setWeek(List<TopRichItem> week) {
        this.week = week;
    }

    public List<TopRichItem> getMonth() {
        return month;
    }

    public void setMonth(List<TopRichItem> month) {
        this.month = month;
    }
}
