package com.xingbo.live.entity;

import java.io.Serializable;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class LoginUser implements Serializable {
    private String id;
    private String nick;
    private String avatar;
    private String livename;

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

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
    }
}
