package com.xingbo.live.entity.anim;

/**
 * Created by WuJinZhou on 2016/3/1.
 */
public class FewGiftAnimMsg {
    private String icon;
    private String num;
    private long time;
    public FewGiftAnimMsg(String icon, String num,long time) {
        this.icon = icon;
        this.num = num;
        this.time=time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
