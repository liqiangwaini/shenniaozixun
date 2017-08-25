package com.xingbo.live.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 礼物数据结构
 * Created by WuJinZhou on 2015/12/9.
 */
public class Gift implements Serializable {
    private String id;
    private String name;
    private String description;
    private String icon;
    private String flv;
    private String consume;
    private String richexp;
    private String gain;
    private String anchorexp;
    private String guardexp;
    private String vipexp;
    private String richlvl_limit;
    private String guardlvl_limit;
    private String viplvl_limit;
    private String active;
    private String classid;
    private String ctime;
    private String sortidx;
    private String num;
    private int lucky_gift = 0;//0 不是，1 是   是否是幸运礼物
    private int week_gift = 0;  //0 不是，1 是   是否是周星礼物
    private int activity_gift = 0;//0 不是，1 是   是否是活动礼物


    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getLucky_gift() {
        return lucky_gift;
    }

    public void setLucky_gift(int lucky_gift) {
        this.lucky_gift = lucky_gift;
    }

    private boolean isSelected = false;
    private boolean isBag = isBag();

    public Gift(String id, String name, String icon, String consume) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.consume = consume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getRichexp() {
        return richexp;
    }

    public void setRichexp(String richexp) {
        this.richexp = richexp;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public String getAnchorexp() {
        return anchorexp;
    }

    public void setAnchorexp(String anchorexp) {
        this.anchorexp = anchorexp;
    }

    public String getGuardexp() {
        return guardexp;
    }

    public void setGuardexp(String guardexp) {
        this.guardexp = guardexp;
    }

    public String getVipexp() {
        return vipexp;
    }

    public void setVipexp(String vipexp) {
        this.vipexp = vipexp;
    }

    public String getRichlvl_limit() {
        return richlvl_limit;
    }

    public void setRichlvl_limit(String richlvl_limit) {
        this.richlvl_limit = richlvl_limit;
    }

    public String getGuardlvl_limit() {
        return guardlvl_limit;
    }

    public void setGuardlvl_limit(String guardlvl_limit) {
        this.guardlvl_limit = guardlvl_limit;
    }

    public String getViplvl_limit() {
        return viplvl_limit;
    }

    public void setViplvl_limit(String viplvl_limit) {
        this.viplvl_limit = viplvl_limit;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getSortidx() {
        return sortidx;
    }

    public void setSortidx(String sortidx) {
        this.sortidx = sortidx;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isBag() {
        return this.num != null && !this.num.equals("");
    }

    public int getWeek_gift() {
        return week_gift;
    }

    public void setWeek_gift(int week_gift) {
        this.week_gift = week_gift;
    }

    public int getActivity_gift() {
        return activity_gift;
    }

    public void setActivity_gift(int activity_gift) {
        this.activity_gift = activity_gift;
    }
}
