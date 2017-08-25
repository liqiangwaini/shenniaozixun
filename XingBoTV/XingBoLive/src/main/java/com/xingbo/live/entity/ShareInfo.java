package com.xingbo.live.entity;

import java.io.Serializable;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class ShareInfo implements Serializable{
    private String title;
    private String desc;
    private String logo;
    private String href;
    private String site_logo;  //微博分享的默认图片

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSite_logo() {
        return site_logo;
    }

    public void setSite_logo(String site_logo) {
        this.site_logo = site_logo;
    }
}
