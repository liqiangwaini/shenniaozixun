package com.xingbo.live.entity;

import java.io.Serializable;

/**
 * 轮播图
 * Created by WuJinZhou on 2015/12/16.
 */
public class PicWall implements Serializable{

    private String id;
    private String title;
    private String logo;
    private String description;
    private String href;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
