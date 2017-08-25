package com.xingbobase.extra.pickerview;

/**
 * Created by WuJinZhou on 2015/8/7.
 */
public class WheelItem {
    public WheelItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
