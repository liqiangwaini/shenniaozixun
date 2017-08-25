package com.xingbo.live.entity.msg;

import com.xingbo.live.entity.UserMedal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WuJinZhou on 2015/12/2.
 */
public class MsgFUser implements Serializable {
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
    private String admin;  //0:普通用户  1:管理员权限
    private String online;
    private long sortidx;   //排序
    private String icons;//游戏图标名称
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

    public MsgFUser(String id, String nick) {
        this.id = id;
        this.nick = nick;
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

    public long getSortidx() {
        return sortidx;
    }

    public void setSortidx(long sortidx) {
        this.sortidx = sortidx;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

}
