package com.xingbo.live.entity;

/**
 * Created by xingbo_szd on 2016/8/2.
 */
public class ChoosePic {
    private String url;
    private boolean isChecked=false;

    ChoosePic(){}
    public ChoosePic(String url){
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
