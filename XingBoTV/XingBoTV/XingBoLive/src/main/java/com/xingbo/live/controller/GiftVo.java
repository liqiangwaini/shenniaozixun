package com.xingbo.live.controller;

/**
 * @author zhongxf
 * @Description 礼物的实体类
 * @Date 2016/6/6.
 */
public class GiftVo {

    private String userId;//送礼物人的UserId
    private String name;//送礼物人的Name
    private String giftname;
    private int num;//送礼物的个数
    GiftVo(){}
    public GiftVo(String userId,String name,String giftname,int num){
        this.userId=userId;
        this.name=name;
        this.giftname=giftname;
        this.num=num;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
