package com.xingbo.live.entity;

import java.io.Serializable;

/**
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeData implements Serializable {
    private String totalGain;
    private String todayGain;

    public String getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(String totalGain) {
        this.totalGain = totalGain;
    }

    public String getTodayGain() {
        return todayGain;
    }

    public void setTodayGain(String todayGain) {
        this.todayGain = todayGain;
    }
}
