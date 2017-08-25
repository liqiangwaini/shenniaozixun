package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class BaseShareContent {
    private String title;
    private String description;
    private String targetUrl;
    protected String imageUrl;

    public BaseShareContent() {

    }

    public BaseShareContent(String title, String description, String targetUrl) {
        this.title = title;
        this.description = description;
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
