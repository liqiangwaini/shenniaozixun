package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFans extends XingBoBaseItem {

    private String id;
    private String nick;
    private String avatar;
    private String richlvl;
    private String anchorlvl;
    private String followers;
    private String livename;
    private String livestatus;
    private String livetime;
    private String liveonlines;
    //新增字段
    private String sex;
    private String intro;
    private boolean followed;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

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

    public String getLivetime() {
        return livetime;
    }

    public void setLivetime(String livetime) {
        this.livetime = livetime;
    }

    public String getLiveonlines() {
        return liveonlines;
    }

    public void setLiveonlines(String liveonlines) {
        this.liveonlines = liveonlines;
    }
}
