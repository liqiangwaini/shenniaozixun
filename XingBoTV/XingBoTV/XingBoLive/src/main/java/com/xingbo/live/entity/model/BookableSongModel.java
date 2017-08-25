package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.SongItem;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/27.
 */
public class BookableSongModel extends BaseResponseModel {
    private List<SongItem> d;

    public List<SongItem> getD() {
        return d;
    }

    public void setD(List<SongItem> d) {
        this.d = d;
    }
}
