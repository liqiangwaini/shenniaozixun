package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/1/19.
 */
public class PopWinBottomMenuItem {
    private int  functionId;//权限id
    private String title;
    private int iconResId;

    public PopWinBottomMenuItem(int functionId, String title, int iconResId) {
        this.functionId = functionId;
        this.title = title;
        this.iconResId = iconResId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
