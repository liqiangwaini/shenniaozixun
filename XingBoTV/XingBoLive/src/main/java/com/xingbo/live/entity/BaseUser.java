package com.xingbo.live.entity;

import java.io.Serializable;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class BaseUser implements Serializable {
    private String id;
    private String nick;
    private String avatar;
    private String intro;//个人简介
    private String sex;
    private String addr;
    private String birth;
    private String followers;//粉丝
    private String followings;//关注
    private String coin;
    private String totalgain;


    private String phone;  //phone 已绑定的手机号
    private String richlvl;
    private  String anchorlvl; //主播等级
    private String authtype;


    public String getAuthtype() {
        return authtype;
    }

    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTotalgain() {
        return totalgain;
    }

    public void setTotalgain(String totalgain) {
        this.totalgain = totalgain;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowings() {
        return followings;
    }

    public void setFollowings(String followings) {
        this.followings = followings;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
