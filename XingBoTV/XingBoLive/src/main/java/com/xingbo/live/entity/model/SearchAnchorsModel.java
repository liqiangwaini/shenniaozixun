package com.xingbo.live.entity.model;

import com.xingbo.live.entity.SearchAnchorsPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/12
 */
public class SearchAnchorsModel extends BaseResponseModel{

    private SearchAnchorsPage d;

    public SearchAnchorsPage getD() {
        return d;
    }

    public void setD(SearchAnchorsPage d) {
        this.d = d;
    }
}
