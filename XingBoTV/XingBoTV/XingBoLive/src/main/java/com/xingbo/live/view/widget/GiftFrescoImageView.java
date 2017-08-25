package com.xingbo.live.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.xingbobase.view.FrescoImageView;

/**
 * Created by WuJinZhou on 2016/3/1.
 */
public class GiftFrescoImageView extends FrescoImageView {
    private int imageIndex;
    private int id;
    private boolean isShow=false;//是否显示在父视图
    private long time;

    public GiftFrescoImageView(Context context, int imageIndex, int id) {
        super(context);
        this.imageIndex = imageIndex;
        this.id = id;
    }

    public GiftFrescoImageView(Context context, AttributeSet attrs, int imageIndex, int id) {
        super(context, attrs);
        this.imageIndex = imageIndex;
        this.id = id;
    }

    public GiftFrescoImageView(Context context, AttributeSet attrs, int defStyle, int imageIndex, int id) {
        super(context, attrs, defStyle);
        this.imageIndex = imageIndex;
        this.id = id;
    }

    public GiftFrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int imageIndex, int id) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.imageIndex = imageIndex;
        this.id = id;
    }

    public GiftFrescoImageView(Context context, GenericDraweeHierarchy hierarchy, int imageIndex, int id) {
        super(context, hierarchy);
        this.imageIndex = imageIndex;
        this.id = id;
    }

    public GiftFrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
