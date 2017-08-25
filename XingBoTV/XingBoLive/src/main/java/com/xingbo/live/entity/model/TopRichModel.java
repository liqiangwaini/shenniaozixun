package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.TopRichCategory;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class TopRichModel extends BaseResponseModel {
    private TopRichCategory d;

    public TopRichCategory getD() {
        return d;
    }

    public void setD(TopRichCategory d) {
        this.d = d;
    }
}
