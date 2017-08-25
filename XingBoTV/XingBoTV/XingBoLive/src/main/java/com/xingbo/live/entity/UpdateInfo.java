package com.xingbo.live.entity;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/29
 */
public class UpdateInfo extends XingBoBaseItem {

    private String id;//安装包ID
    private String package_name;//安装包包名
    private String package_url;//安装包路径
    private String package_version;//版本
    private String type;//类型，1 安卓，2 苹果
    private String ctime;//添加时间
    private String utime;//更新时间
    private String package_desc;//版本信息介绍
    private String ios_auditing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_url() {
        return package_url;
    }

    public void setPackage_url(String package_url) {
        this.package_url = package_url;
    }

    public String getPackage_version() {
        return package_version;
    }

    public void setPackage_version(String package_version) {
        this.package_version = package_version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getPackage_desc() {
        return package_desc;
    }

    public void setPackage_desc(String package_desc) {
        this.package_desc = package_desc;
    }

    public String getIos_auditing() {
        return ios_auditing;
    }

    public void setIos_auditing(String ios_auditing) {
        this.ios_auditing = ios_auditing;
    }
}
