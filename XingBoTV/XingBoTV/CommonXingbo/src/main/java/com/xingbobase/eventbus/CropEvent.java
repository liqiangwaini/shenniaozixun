package com.xingbobase.eventbus;

/**
 * Created by WuJinZhou on 2016/1/11.
 */
public class CropEvent {
    private int sourceTagCode;
    private String path;

    public CropEvent(int sourceTagCode, String path) {
        this.sourceTagCode = sourceTagCode;
        this.path = path;
    }

    public int getSourceTagCode() {
        return sourceTagCode;
    }

    public void setSourceTagCode(int sourceTagCode) {
        this.sourceTagCode = sourceTagCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
