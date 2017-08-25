package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class TopGiftCategory {
    private List<TopGiftItem>send;
    private List<TopGiftItem>recv;

    public List<TopGiftItem> getSend() {
        return send;
    }

    public void setSend(List<TopGiftItem> send) {
        this.send = send;
    }

    public List<TopGiftItem> getRecv() {
        return recv;
    }

    public void setRecv(List<TopGiftItem> recv) {
        this.recv = recv;
    }
}
