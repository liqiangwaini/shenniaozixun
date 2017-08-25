package com.xingbo.live.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.xingbobase.entity.XingBoBaseItem;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class UserPhotos extends XingBoBaseItem implements Parcelable {
    private  String id;//照片id；
    private  String url;//照片地址
    private  String  uptime;//上传时间

    protected UserPhotos(Parcel in) {
        id = in.readString();
        url = in.readString();
        uptime = in.readString();
    }

    public static final Creator<UserPhotos> CREATOR = new Creator<UserPhotos>() {
        @Override
        public UserPhotos createFromParcel(Parcel in) {
            return new UserPhotos(in);
        }

        @Override
        public UserPhotos[] newArray(int size) {
            return new UserPhotos[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(uptime);
    }
}
