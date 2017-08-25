package com.xingbo.live.entity;

import com.umeng.socialize.media.BaseMediaObject;

/**
 * Created by WuJinZhou on 2016/1/13.
 */
public class MediaShareContent<T extends BaseMediaObject> {
    private String title;
    private String description;
    private String tagUrl;
    private T media;

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

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public T getMedia() {
        return media;
    }

    public void setMedia(T media) {
        this.media = media;
    }
}
