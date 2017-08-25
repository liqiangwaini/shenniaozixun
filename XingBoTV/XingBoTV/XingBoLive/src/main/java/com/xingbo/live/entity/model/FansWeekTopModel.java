package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.WeekFans;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
@Deprecated
public class FansWeekTopModel extends BaseResponseModel {
    private List<WeekFans>d;

    public List<WeekFans> getD() {
        return d;
    }

    public void setD(List<WeekFans> d) {
        this.d = d;
    }
}
