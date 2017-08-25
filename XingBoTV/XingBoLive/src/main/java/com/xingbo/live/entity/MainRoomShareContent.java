package com.xingbo.live.entity;

import com.xingbo.live.http.HttpConfig;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class MainRoomShareContent extends BaseShareContent {
    private String logo;
    public MainRoomShareContent(String title, String description, String targetUrl, String logo) {
        super(title, description, targetUrl);
        this.logo = logo;
    }

    public String getLogo() {
        return HttpConfig.FILE_SERVER+logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
