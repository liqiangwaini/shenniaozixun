package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.Guard;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class GuardModel extends BaseResponseModel {
    private List<Guard>d;

    public List<Guard> getD() {
        return d;
    }

    public void setD(List<Guard> d) {
        this.d = d;
    }
}
