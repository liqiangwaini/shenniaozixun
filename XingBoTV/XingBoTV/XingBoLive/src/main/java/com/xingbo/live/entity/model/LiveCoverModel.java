package com.xingbo.live.entity.model;

import com.xingbo.live.entity.LiveCover;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class LiveCoverModel extends BaseResponseModel {
    private LiveCover d;

    public LiveCover getD() {
        return d;
    }

    public void setD(LiveCover d) {
        this.d = d;
    }
}
