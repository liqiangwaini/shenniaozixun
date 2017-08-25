package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/12
 */
public class SearchAnchors extends XingBoBaseItem {

    private  String id;//主播id
    private  String nick;//主播昵称
    private  String avatar;//
    private  String addr;//
    private  String anchorlvl;//主播等级
    private  String livename;//
    private String followers;
    private  String liveLogo;//
    private   int livestatus;//直播状态，0 未开播，1 正在直播
    private   String liveonlines;//直播人数
    private  String livetime;//上次开播时间
    private  String liveclass;
    private  String posterlogo;//艺术照封面
    private  String livemood;
    private  String consumehot;//热度
    private RoomInfo roominfo;//房间信息

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getLiveLogo() {
        return liveLogo;
    }

    public void setLiveLogo(String liveLogo) {
        this.liveLogo = liveLogo;
    }

    public int getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(int livestatus) {
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

    public String getLiveclass() {
        return liveclass;
    }

    public void setLiveclass(String liveclass) {
        this.liveclass = liveclass;
    }

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

    public String getConsumehot() {
        return consumehot;
    }

    public void setConsumehot(String consumehot) {
        this.consumehot = consumehot;
    }

    public RoomInfo getRoominfo() {
        return roominfo;
    }

    public void setRoominfo(RoomInfo roominfo) {
        this.roominfo = roominfo;
    }
}
