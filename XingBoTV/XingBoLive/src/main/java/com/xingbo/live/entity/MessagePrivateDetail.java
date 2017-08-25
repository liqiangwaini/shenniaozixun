package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class MessagePrivateDetail extends XingBoBaseItem{
    private String id;//消息
    private String lastmsg;//消息内容
    private String lastsender;//最后一条发送人id
    private String freadflag;//发送人是否已读
    private String treadflag;//接收人是否已读
    private String ctime;//发送时间

    public MessagePrivateDetail(){
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
