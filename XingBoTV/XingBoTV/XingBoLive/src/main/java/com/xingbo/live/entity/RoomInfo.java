package com.xingbo.live.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/15.
 */
public class RoomInfo implements Serializable {
    private String live;//直播视频源url
    private String notify;//聊天室url  //websocket 连接地址
    private ShareInfo share;

    private boolean followed;//关注主播状态
    private String ownerid;
    private String coin;//用户余额，未登录为零
    private int flower;//用户星星数量
    private String logintype;//是否登录"login"

    private int richlvl;//当前用户财富等级
    private int viplvl;//当前用户会员等级
    private int guardlvl;//当前用户守护等级

    private Anchor anchor;
    private String laba_price;//广播一次价格
    private String song_price;//点一首歌价格
    private String flyword_price;//飞屏一次价格

    private int superadmin;//超管标识：1 巡管，2 官方，0 普通用户

    private List<String> green_msg_list;  //绿色直播消息列表;

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public int getFlower() {
        return flower;
    }

    public void setFlower(int flower) {
        this.flower = flower;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public String getLaba_price() {
        return laba_price;
    }

    public void setLaba_price(String laba_price) {
        this.laba_price = laba_price;
    }

    public String getSong_price() {
        return song_price;
    }

    public void setSong_price(String song_price) {
        this.song_price = song_price;
    }

    public String getFlyword_price() {
        return flyword_price;
    }

    public void setFlyword_price(String flyword_price) {
        this.flyword_price = flyword_price;
    }

    public ShareInfo getShare() {
        return share;
    }

    public void setShare(ShareInfo share) {
        this.share = share;
    }

    public int getRichlvl() {
        return richlvl;
    }

    public void setRichlvl(int richlvl) {
        this.richlvl = richlvl;
    }

    public int getViplvl() {
        return viplvl;
    }

    public void setViplvl(int viplvl) {
        this.viplvl = viplvl;
    }

    public int getGuardlvl() {
        return guardlvl;
    }

    public void setGuardlvl(int guardlvl) {
        this.guardlvl = guardlvl;
    }

    public List<String> getGreen_msg_list() {
        return green_msg_list;
    }

    public void setGreen_msg_list(List<String> green_msg_list) {
        this.green_msg_list = green_msg_list;
    }

    public int getSuperadmin() {
        return superadmin;
    }

    public void setSuperadmin(int superadmin) {
        this.superadmin = superadmin;
    }
}
