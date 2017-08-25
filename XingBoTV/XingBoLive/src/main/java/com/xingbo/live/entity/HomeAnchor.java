package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class HomeAnchor extends XingBoBaseItem {
    private String id;
    private String nick;
    private String anchorlvl;
    private String livename;
    private String livelogo;
    private String livestatus;
    private String liveonlines;
    private String livetime;
    private String addr;
    private String avatar;
    private String posterlogo;
    private String livemood;

    public String getPosterlogo() {
        return posterlogo;
    }

    public void setPosterlogo(String posterlogo) {
        this.posterlogo = posterlogo;
    }

    public String getLivemood() {
        return livemood;
    }

    public void setLivemood(String livemood) {
        this.livemood = livemood;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    private boolean isChecked=false;


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

    public String getLivelogo() {
        return livelogo;
    }

    public void setLivelogo(String livelogo) {
        this.livelogo = livelogo;
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

    public String getLivetime() {
        return livetime;
    }

    public void setLivetime(String livetime) {
        this.livetime = livetime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
