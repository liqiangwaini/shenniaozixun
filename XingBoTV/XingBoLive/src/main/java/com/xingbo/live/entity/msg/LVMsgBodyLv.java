package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2015/12/10.
 */
public class LVMsgBodyLv {

    private int lvl;
    private int exp;
    private String name;
    private int left;
    private float ratio;
    private int curexp;

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getCurexp() {
        return curexp;
    }

    public void setCurexp(int curexp) {
        this.curexp = curexp;
    }
}
