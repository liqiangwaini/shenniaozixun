package com.xingbo.live.entity.msg;

/**
 * Created by xingbo_szd on 2016/9/23.
 */
public class CancelUserBagItemBody {
    private int bagnum;  //背包中还有多少个
    private String gid;  //礼物ID
    private int spen_num;  //本次消耗了多少个

    public int getBagnum() {
        return bagnum;
    }

    public void setBagnum(int bagnum) {
        this.bagnum = bagnum;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getSpen_num() {
        return spen_num;
    }

    public void setSpen_num(int spen_num) {
        this.spen_num = spen_num;
    }
}
