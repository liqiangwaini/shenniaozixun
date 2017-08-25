package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class MainUser {
    private String id;
    private String nick;
    private String avatar;
    private String intro;
    private String sex;
    private String addr;
    private String birth;
    private String followers;//粉丝数
    private String followings;//关注数
    private String livename;
    private String livestatus;
    private String livetime;
    private String richlvl;
    private String anchorlvl;
    private String richleft;
    private String richratio;
    private String anchorleft;
    private String anchorratio;
    private List<UserMedal> medals;
    private boolean followed;//是否已关注
    private boolean isself;
    private int coin;

    private int allow_manage;  //是否允许管理   1允许  0禁止
    private int addAdmin;  //是否可添加管理员
    private int cancelAdmin;  //是否可取消管理员
    private int addMute;  //是否可禁言
    private int cancelMute;  //是否可取消禁言
    private int addKick;  //是否可踢出房间
    private int stoplive;  //是否可停止直播

    private String closeId;  //亲密用户ID
    private String closeAvatar;  //亲密用户头像
    private String closeNick;  //亲密用户昵称

    public String getCloseId() {
        return closeId;
    }

    public void setCloseId(String closeId) {
        this.closeId = closeId;
    }

    public String getCloseAvatar() {
        return closeAvatar;
    }

    public void setCloseAvatar(String closeAvatar) {
        this.closeAvatar = closeAvatar;
    }

    public String getCloseNick() {
        return closeNick;
    }

    public void setCloseNick(String closeNick) {
        this.closeNick = closeNick;
    }

    public boolean isself() {
        return isself;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
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

    public String getRichleft() {
        return richleft;
    }

    public void setRichleft(String richleft) {
        this.richleft = richleft;
    }

    public String getRichratio() {
        return richratio;
    }

    public void setRichratio(String richratio) {
        this.richratio = richratio;
    }

    public String getAnchorleft() {
        return anchorleft;
    }

    public void setAnchorleft(String anchorleft) {
        this.anchorleft = anchorleft;
    }

    public String getAnchorratio() {
        return anchorratio;
    }

    public void setAnchorratio(String anchorratio) {
        this.anchorratio = anchorratio;
    }

    public List<UserMedal> getMedals() {
        return medals;
    }

    public void setMedals(List<UserMedal> medals) {
        this.medals = medals;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isIsself() {
        return isself;
    }

    public void setIsself(boolean isself) {
        this.isself = isself;
    }

    public int getAllow_manage() {
        return allow_manage;
    }

    public void setAllow_manage(int allow_manage) {
        this.allow_manage = allow_manage;
    }

    public int getAddAdmin() {
        return addAdmin;
    }

    public void setAddAdmin(int addAdmin) {
        this.addAdmin = addAdmin;
    }

    public int getCancelAdmin() {
        return cancelAdmin;
    }

    public void setCancelAdmin(int cancelAdmin) {
        this.cancelAdmin = cancelAdmin;
    }

    public int getAddMute() {
        return addMute;
    }

    public void setAddMute(int addMute) {
        this.addMute = addMute;
    }

    public int getCancelMute() {
        return cancelMute;
    }

    public void setCancelMute(int cancelMute) {
        this.cancelMute = cancelMute;
    }

    public int getAddKick() {
        return addKick;
    }

    public void setAddKick(int addKick) {
        this.addKick = addKick;
    }

    public int getStoplive() {
        return stoplive;
    }

    public void setStoplive(int stoplive) {
        this.stoplive = stoplive;
    }
}
