package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/1/26.
 */
public class RoomUserInfo {
    private String id;
    private String nick;
    private String avatar;
    private String sex;
    private String birth;
    private String addr;
    private String intro;
    private String richlvl;
    private String anchorlvl;
    private String viplvl;
    private String richleft;
    private String richratio;//当前财富进度0~1
    private String anchorleft;
    private String anchorratio;
    private String livename;
    private String [] privs;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
    }

    public String[] getPrivs() {
        return privs;
    }

    public void setPrivs(String[] privs) {
        this.privs = privs;
    }
}
