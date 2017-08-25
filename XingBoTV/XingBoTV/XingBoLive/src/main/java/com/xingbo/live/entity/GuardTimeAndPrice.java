package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/3/16.
 */
public class GuardTimeAndPrice {

    private int month=1;
    private int price;
    private String expire;
    private String coin;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
