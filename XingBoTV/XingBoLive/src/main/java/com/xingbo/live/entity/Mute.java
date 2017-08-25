package com.xingbo.live.entity;

/**
 * 禁言
 */
public class Mute {
    private int type;  //0:本场禁言  1:永久禁言
    private String nickname;
    Mute(){
    }
    public Mute(int type,String nickname){
        this.type=type;
        this.nickname=nickname;

    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
