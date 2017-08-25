package com.xingbo.live.eventbus;

import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.entity.User;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class UserEditEvent {
    public final static int UPDATE_HEADER=0x1;
    public final static int UPDATE_NICK=0x2;
    public final static int UPDATE_BIRTH=0x3;
    public final static int UPDATE_SEX=0x4;
    public final static int UPDATE_ADDRESS=0x5;
    public final static int UPDATE_INTRO=0x6;


    private int code;
    private BaseUser user;

    public UserEditEvent(){

    }

    public UserEditEvent(int code, BaseUser user) {
        this.code = code;
        this.user= user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }
}
