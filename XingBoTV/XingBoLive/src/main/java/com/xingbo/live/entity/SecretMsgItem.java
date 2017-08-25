package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/7/29.
 */
public class SecretMsgItem {
    private String id;
    private String lastmsg;
    private String lastsender;
    private String freadflag;
    private String treadflag;
    private String ctime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFreadflag() {
        return freadflag;
    }

    public void setFreadflag(String freadflag) {
        this.freadflag = freadflag;
    }

    public String getTreadflag() {
        return treadflag;
    }

    public void setTreadflag(String treadflag) {
        this.treadflag = treadflag;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
