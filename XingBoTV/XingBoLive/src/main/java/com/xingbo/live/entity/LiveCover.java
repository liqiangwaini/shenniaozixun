package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/8/16.
 */
public class LiveCover {
    private String id;
    private String nick;
    private String livename;
    private String posterlogo;
    private String livemood;
    private String app_publish_linetype;

    public String getApp_publish_linetype() {
        return app_publish_linetype;
    }

    public void setApp_publish_linetype(String app_publish_linetype) {
        this.app_publish_linetype = app_publish_linetype;
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

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
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
}
