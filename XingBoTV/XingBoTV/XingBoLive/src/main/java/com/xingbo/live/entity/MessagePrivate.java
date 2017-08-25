package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * 私信列表item
 */
public class MessagePrivate extends XingBoBaseItem {
    private String sessionid;//消息会话id
    private String lastmsg;//消息内容
    private  String lastsender;//最后一条发送人 id
    private  String ctime;//发送时间
    private  String  uid;//发送人id
    private  String  nick;//昵称
    private  String avatar;//头像
    private String lastnick;//最后一条发送人昵称
    //缺少性别和等级
    private  String sex;
    private String richlevel;

    private String noreadcnt;//未读条数
    private int readflag;  //已读状态 0未读 1已读

    public String getNoreadcnt() {
        return noreadcnt;
    }

    public void setNoreadcnt(String noreadcnt) {
        this.noreadcnt = noreadcnt;
    }

    public int getReadflag() {
        return readflag;
    }

    public void setReadflag(int readflag) {
        this.readflag = readflag;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRichlevel() {
        return richlevel;
    }

    public void setRichlevel(String richlevel) {
        this.richlevel = richlevel;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getLastsender() {
        return lastsender;
    }

    public void setLastsender(String lastsender) {
        this.lastsender = lastsender;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getLastnick() {
        return lastnick;
    }

    public void setLastnick(String lastnick) {
        this.lastnick = lastnick;
    }
}
