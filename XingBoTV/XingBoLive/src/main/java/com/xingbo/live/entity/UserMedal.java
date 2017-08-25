package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserMedal extends XingBoBaseItem {
    private String id;
    private String name;
    private String description;
    private String icon;
    private String expire;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
