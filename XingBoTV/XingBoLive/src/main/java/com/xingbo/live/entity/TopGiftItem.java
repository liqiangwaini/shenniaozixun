package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class TopGiftItem extends XingBoBaseItem {
    private boolean isBar=false;
    private String nick;
    private String avatar;
    private String richlvl;
    private String anchorlvl;
    private String livename;
    private String livestatus;
    private String uid;
    private String value;
    private String gid;
    private String gname;
    private String gicon;

    public TopGiftItem(boolean isBar,int type) {
        this.isBar = isBar;
        this.setItemType(type);
    }

    public boolean isBar() {
        return isBar;
    }

    public void setBar(boolean isBar) {
        this.isBar = isBar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRichlvl() {
        return richlvl;
    }

    public void setRichlvl(String richlvl) {
        this.richlvl = richlvl;
    }

    public String getAnchorlvl() {
        return anchorlvl;
    }

    public void setAnchorlvl(String anchorlvl) {
        this.anchorlvl = anchorlvl;
    }

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
    }

    public String getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(String livestatus) {
        this.livestatus = livestatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGicon() {
        return gicon;
    }

    public void setGicon(String gicon) {
        this.gicon = gicon;
    }
}
