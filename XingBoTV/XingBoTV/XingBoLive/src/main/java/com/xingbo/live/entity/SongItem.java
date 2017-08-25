package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Created by WuJinZhou on 2016/1/27.
 */
public class SongItem extends XingBoBaseItem {
    public final static int SONG_STATE_BOOKING=0x1;//等待主播处理状态[拒绝或同意]
    public final static int SONG_STATE_AGREED=0x2;//已同意==已播放状态
    public final static int SONG_STATE_REFUSED=-0x1;//主播已拒绝状态

    private String id;
    private String name;
    private String author;
    private String num;
    private String ctime;

    private String userid;
    private String uname;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
