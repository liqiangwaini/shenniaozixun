package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.TopGiftCategory;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class TopGiftModel extends BaseResponseModel {
    private TopGiftCategory d;

    public TopGiftCategory getD() {
        return d;
    }

    public void setD(TopGiftCategory d) {
        this.d = d;
    }
}
