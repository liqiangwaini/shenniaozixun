package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/1/11.
 */
public class Anchor extends XingBoBaseItem {
    private String id;
    private String nick;
    private String avator;
    private String richlvl;
    private int anchorexp;  //主播经验值
    private String anchorlvl;
    private String flowergift;
    private String followers;
    private String livename;
    private String livestatus;
    private String liveonlines;

    private String posterlogo;  //艺术照
    private String livelogo;  //封面照


    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avator;
    }

    public void setAvatar(String avator) {
        this.avator = avator;
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

    public String getFlowergift() {
        return flowergift;
    }

    public void setFlowergift(String flowergift) {
        this.flowergift = flowergift;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
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

    public String getLiveonlines() {
        return liveonlines;
    }

    public void setLiveonlines(String liveonlines) {
        this.liveonlines = liveonlines;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getAnchorexp() {
        return anchorexp;
    }

    public void setAnchorexp(int anchorexp) {
        this.anchorexp = anchorexp;
    }

    public String getPosterlogo() {
        return posterlogo;
    }

    public void setPosterlogo(String posterlogo) {
        this.posterlogo = posterlogo;
    }

    public String getLivelogo() {
        return livelogo;
    }

    public void setLivelogo(String livelogo) {
        this.livelogo = livelogo;
    }
}
