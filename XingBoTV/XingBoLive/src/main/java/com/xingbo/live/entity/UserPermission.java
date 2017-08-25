package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class UserPermission {
    private String sendPub;
    private String sendPri;
    private  String sendGift;
    private String report;
    private String addFollow;

    public String getSendPub() {
        return sendPub;
    }

    public void setSendPub(String sendPub) {
        this.sendPub = sendPub;
    }

    public String getSendPri() {
        return sendPri;
    }

    public void setSendPri(String sendPri) {
        this.sendPri = sendPri;
    }

    public String getSendGift() {
        return sendGift;
    }

    public void setSendGift(String sendGift) {
        this.sendGift = sendGift;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getAddFollow() {
        return addFollow;
    }

    public void setAddFollow(String addFollow) {
        this.addFollow = addFollow;
    }
}
