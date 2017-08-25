package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/8/19.
 */
public class CloseLive {
    private int time_span;  //本次直播时长（分钟）
    private int watch_num;  //本次观看人数（个数）

    public int getTime_span() {
        return time_span;
    }

    public void setTime_span(int time_span) {
        this.time_span = time_span;
    }

    public int getWatch_num() {
        return watch_num;
    }

    public void setWatch_num(int watch_num) {
        this.watch_num = watch_num;
    }
}
