package com.xingbo.live.entity.msg;

import com.xingbo.live.entity.UserMedal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WuJinZhou on 2015/12/2.
 */
public class MsgTUser implements Serializable {
    private String id;
    private String nick;
    private String avatar;
    private String richlvl;
    private String anchorlvl;
    private String viplvl;
    private String mountid;
    private String superadmin;
    private String logintype;
    private String guardlvl;
    private String admin;
    private String online;
    private String sortidx;
    private String livename;

    private List<UserMedal> usermedals;

    public List<UserMedal> getUsermedals() {
        return usermedals;
    }

    public void setUsermedals(List<UserMedal> usermedals) {
        this.usermedals = usermedals;
    }

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
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

    public String getViplvl() {
        return viplvl;
    }

    public void setViplvl(String viplvl) {
        this.viplvl = viplvl;
    }

    public String getMountid() {
        return mountid;
    }

    public void setMountid(String mountid) {
        this.mountid = mountid;
    }

    public String getSuperadmin() {
        return superadmin;
    }

    public void setSuperadmin(String superadmin) {
        this.superadmin = superadmin;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getGuardlvl() {
        return guardlvl;
    }

    public void setGuardlvl(String guardlvl) {
        this.guardlvl = guardlvl;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSortidx() {
        return sortidx;
    }

    public void setSortidx(String sortidx) {
        this.sortidx = sortidx;
    }
}
